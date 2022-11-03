rootProject.name = "MovieOn"
include("server")
include("shared")
include(
    "user-api",
    "user-api:user-interfaces",
    "user-api:user-application",
    "user-api:user-domain",
    "user-api:user-infrastructure",
)
include(
    "notification-api",
    "notification-api:notification-interfaces",
    "notification-api:notification-application",
    "notification-api:notification-domain",
    "notification-api:notification-infrastructure",
)
include(
    "order-api",
    "order-api:order-interfaces",
    "order-api:order-application",
    "order-api:order-domain",
    "order-api:order-infrastructure",
)
include("product-api")
include("payment-api")
include("query-api")
include("batch-api")
include("playground")

pluginManagement {
    val kotlinVersion: String by settings
    val springBootVersion: String by settings
    val springDependencyManagementVersion: String by settings

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.jetbrains.kotlin.jvm" -> useVersion(kotlinVersion)
                "org.jetbrains.kotlin.plugin.spring" -> useVersion(kotlinVersion)
                "org.jetbrains.kotlin.plugin.jpa" -> useVersion(kotlinVersion)
                "org.springframework.boot" -> useVersion(springBootVersion)
                "io.spring.dependency-management" -> useVersion(springDependencyManagementVersion)
            }
        }
    }
}
