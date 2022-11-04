package com.remember.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfiguration {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(*SWAGGER_URI).permitAll()
            .antMatchers(HttpMethod.POST, REGISTER_URI).permitAll()
            .antMatchers(HttpMethod.GET, REGISTER_CONFIRM_URI).permitAll()
            .antMatchers(HttpMethod.POST, LOGIN_URI).permitAll()
            .antMatchers(HttpMethod.POST, REISSUANCE_API).permitAll()
            .anyRequest().authenticated()
        return http.build()
    }
}

private val SWAGGER_URI = arrayOf("/v3/api-docs/**", "/swagger-ui/**", "/api/v1/swagger")
private const val REGISTER_URI = "/api/v1/users/register"
private const val REGISTER_CONFIRM_URI = "/api/v1/users/register/confirm"
private const val LOGIN_URI = "/api/v1/users/login"
private const val REISSUANCE_API = "/api/v1/users/reIssuance"
