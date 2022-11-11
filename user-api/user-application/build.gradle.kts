tasks.getByName("bootJar") {
    enabled = false
}

tasks.getByName("jar") {
    enabled = true
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":user-api:user-domain"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}
