tasks.getByName("bootJar") {
    enabled = false
}

tasks.getByName("jar") {
    enabled = true
}

dependencies {
    implementation(project(":notification-api:notification-domain"))
    implementation(project(":shared"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}
