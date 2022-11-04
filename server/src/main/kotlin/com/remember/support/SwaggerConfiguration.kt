package com.remember.support

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Configuration
@Profile(value = ["local", "bravo", "alpha"])
class SwaggerConfiguration {

    @Controller
    @RequestMapping("/api/v1/swagger")
    @Profile(value = ["local", "bravo", "alpha"])
    class RedirectController {

        @GetMapping
        fun swagger(): String {
            return "redirect:/swagger-ui/index.html"
        }
    }

    @Bean
    fun openApi(
        @Value("\${spring.profiles.active}") profile: String,
    ): OpenAPI {
        val info = info(profile)
        val server = Server().url("http://localhost:8081")
            .description("실행 환경 $profile")
        val servers = listOf(server)
        return OpenAPI()
            .components(
                Components()
                    .addSecuritySchemes("bearer-authentication", securityScheme())
            )
            .info(info)
            .security(listOf(SecurityRequirement().addList("bearer-authentication")))
            .servers(servers)
    }

    private fun securityScheme(): SecurityScheme {
        return SecurityScheme()
            .type(SecurityScheme.Type.HTTP).scheme("bearer")
            .bearerFormat("JWT")
            .`in`(SecurityScheme.In.HEADER).name("Authorization")
    }

    private fun info(profile: String): Info {
        return Info()
            .title("MovieOn API 문서입니다. 실행 환경($profile)")
            .description("Kotlin, Spring에 익숙해지기 위한 온보딩 프로젝트입니다.")
            .version("0.1")
    }
}
