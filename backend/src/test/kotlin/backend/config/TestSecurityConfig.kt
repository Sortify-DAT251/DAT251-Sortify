package backend.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@TestConfiguration
@EnableWebSecurity
class TestSecurityConfig {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        // Allow all requests
        http
            .authorizeHttpRequests { authz ->
                authz.anyRequest().permitAll()  // Allow all requests without authentication
            }
            .csrf { csrf -> csrf.disable() }  // Optionally disable CSRF protection for testing

        return http.build()  // Finalize the configuration
    }
}