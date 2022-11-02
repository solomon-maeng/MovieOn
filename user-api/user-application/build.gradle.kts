tasks.getByName("bootJar") {
    enabled = false
}

tasks.getByName("jar") {
    enabled = true
}

dependencies {
    implementation(project(":user-api:user-domain"))
    implementation(project(":shared"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}
