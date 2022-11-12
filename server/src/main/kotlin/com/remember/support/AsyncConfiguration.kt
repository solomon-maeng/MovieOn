package com.remember.support

import com.remember.shared.getLogger
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

@EnableAsync
@Configuration
class AsyncConfiguration : AsyncConfigurer {

    private val log = getLogger()

    override fun getAsyncExecutor(): Executor {
        val executor = ThreadPoolTaskExecutor()
        val processors = Runtime.getRuntime().availableProcessors()
        log.info("processors count {}", processors)
        executor.corePoolSize = processors
        executor.maxPoolSize = processors * 2
        executor.queueCapacity = 50
        executor.keepAliveSeconds = 60
        executor.setThreadNamePrefix("AsyncExecutor-")
        executor.initialize()
        return executor
    }
}
