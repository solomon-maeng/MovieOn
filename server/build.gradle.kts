tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":query-api"))
    implementation(project(":batch-api"))
    implementation(project(":payment-api"))
    implementation(project(":product-api"))
    implementation(project(":order-api"))
    implementation(project(":notification-api"))
    implementation(project(":user-api:user-interfaces"))
    implementation(project(":user-api:user-application"))
    implementation(project(":user-api:user-domain"))
    implementation(project(":user-api:user-infrastructure"))

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    implementation("org.springframework.cloud:spring-cloud-starter-aws-messaging:2.2.6.RELEASE")
    implementation("org.springframework.cloud:spring-cloud-starter-aws-secrets-manager-config:2.2.6.RELEASE")

    implementation("org.springdoc:springdoc-openapi-ui:1.6.12")
    implementation("org.springdoc:springdoc-openapi-security:1.6.12")

    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")

    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    runtimeOnly("mysql:mysql-connector-java")

    testImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter:0.4.1")
    testImplementation("org.testcontainers:mysql:1.17.4")
    testImplementation("org.testcontainers:testcontainers:1.17.4")
    testImplementation("com.ninja-squad:springmockk:3.1.1")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")
    testImplementation("io.kotest.extensions:kotest-extensions-testcontainers:1.3.4")
}
