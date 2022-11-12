package com.remember.support

import com.ninjasquad.springmockk.MockkBean
import com.remember.shared.MessageBus
import io.kotest.core.spec.style.DescribeSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.support.TransactionTemplate
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MySQLContainer

@IntegrationSpec
@ContextConfiguration(initializers = [IntegrationSpecHelper.InfrastructureInitializer::class])
abstract class IntegrationSpecHelper: DescribeSpec() {

    @LocalServerPort
    protected var port: Int = 0

    @MockkBean(relaxed = true)
    protected lateinit var messageBus: MessageBus

    @Autowired protected lateinit var client: TestRestTemplate
    @Autowired protected lateinit var cleaner: DatabaseCleaner
    @Autowired protected lateinit var transaction: TransactionTemplate

    companion object {
        // TODO 아래 구성들을 LocalStack 포함하여 DockerComposeContainer 사용하도록 변경
        val redisContainer = GenericContainer<Nothing>("redis:3-alpine")
            .apply {
                withExposedPorts(6379)
            }
        val mysqlContainer = MySQLContainer<Nothing>("mysql:8.0.30")
            .apply {
                withDatabaseName("movieon")
                withUsername("joyful")
                withPassword("helloworld!")
                withUrlParam("serverTimeZone", "UTC")
            }
    }

    internal class InfrastructureInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            redisContainer.start()
            mysqlContainer.start()

            TestPropertyValues.of(
                "spring.redis.host=${redisContainer.host}",
                "spring.redis.port=${redisContainer.firstMappedPort}",
                "spring.datasource.url=${mysqlContainer.jdbcUrl}",
                "spring.datasource.username=${mysqlContainer.username}",
                "spring.datasource.password=${mysqlContainer.password}",
                "spring.datasource.driver-class-name=${mysqlContainer.driverClassName}",
            ).applyTo(applicationContext.environment)
        }
    }
}
