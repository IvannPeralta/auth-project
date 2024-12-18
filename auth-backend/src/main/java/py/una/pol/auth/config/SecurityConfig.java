package py.una.pol.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**", // Allow Swagger UI
                                "/v3/api-docs/**", // Allow OpenAPI documentation
                                "/swagger-ui.html" // Allow direct Swagger URL
                        ).permitAll()
                        .anyRequest().authenticated())
                .httpBasic(); // Basic authentication for other endpoints

        return http.build();
    }
}
// para acceder a http://localhost:8080/swagger-ui.html es este codigo
