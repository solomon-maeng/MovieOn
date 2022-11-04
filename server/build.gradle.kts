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
    implementation(project(":user-api"))

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("org.springdoc:springdoc-openapi-ui:1.6.11")

    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")

    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    runtimeOnly("mysql:mysql-connector-java")
    testRuntimeOnly("com.h2database:h2")
}
