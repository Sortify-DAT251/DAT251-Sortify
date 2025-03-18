// package backend.config

// import org.springframework.context.annotation.Bean
// import org.springframework.context.annotation.Configuration
// import org.springframework.http.HttpMethod
// import org.springframework.security.config.annotation.web.builders.HttpSecurity
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

// @Configuration
// @EnableWebSecurity
// class SecurityConfig : WebSecurityConfigurerAdapter() {

//     override fun configure(http: HttpSecurity) {
//         http
//             .csrf().disable() // Disable CSRF for simplicity (not recommended for production)
//             .authorizeRequests()
//             .antMatchers("/auth/login").permitAll() // Allow login endpoint without authentication
//             .anyRequest().authenticated() // Protect all other endpoints
//             .and()
//             .addFilterBefore(JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
//     }
// }