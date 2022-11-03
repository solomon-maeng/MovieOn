allOpen {
    annotation("javax.persistence.MappedSuperclass")
}

tasks.getByName("bootJar") {
    enabled = false
}

tasks.getByName("jar") {
    enabled = true
}

dependencies {
    implementation("org.springframework.data:spring-data-envers")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}
