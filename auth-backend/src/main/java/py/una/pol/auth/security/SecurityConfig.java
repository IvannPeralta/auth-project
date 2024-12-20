package py.una.pol.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;



@Configuration
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Disable CSRF
            .authorizeHttpRequests(authz -> authz
                .requestMatchers(
                    "/swagger-ui/**",        // Allow access to Swagger UI resources
                    "/api-docs/**"           // Allow access to the Swagger docs
                ).permitAll()
                .requestMatchers("/api/users/register").permitAll() // Allow user registration
                .requestMatchers("/api/auth/login").permitAll() // Allow login
                .requestMatchers(HttpMethod.POST, "/api/roles").permitAll() // Role creation requires ADMIN authority
                .requestMatchers(HttpMethod.GET, "/api/roles").permitAll() // List roles requires ADMIN authority
                .requestMatchers(HttpMethod.GET, "/api/roles/{id}").permitAll() // Get role by ID requires ADMIN authority
                .requestMatchers(HttpMethod.PUT, "/api/roles/{id}").permitAll() // Update roles requires ADMIN authority
                .requestMatchers(HttpMethod.POST, "/api/permissions").permitAll() // Create permissions requires ADMIN authority
                .requestMatchers(HttpMethod.GET, "/api/permissions/{id}").permitAll() // Get permission by ID requires ADMIN authority
                .requestMatchers(HttpMethod.GET, "/api/permissions").permitAll() // List permissions requires ADMIN authority
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll() // Create users requires ADMIN authority
                .requestMatchers(HttpMethod.GET, "/api/users").permitAll() // List users requires ADMIN authority
                .requestMatchers(HttpMethod.POST, "/api/roles/{roleId}/permissions").permitAll() // Assign permissions to roles requires ADMIN authority
                .requestMatchers(HttpMethod.PUT, "/api/roles/{roleId}/permissions").permitAll() // Assign permissions to roles requires ADMIN authority
                .requestMatchers(HttpMethod.GET, "/api/roles/{roleId}/permissions").permitAll() // List permissions for roles requires ADMIN authority
                .requestMatchers(HttpMethod.POST, "/api/users/{roleId}/roles").permitAll() // Assign roles to users requires ADMIN authority
                .requestMatchers(HttpMethod.GET, "/api/users/{roleId}/roles").permitAll() // Assign roles to users requires ADMIN authority
                .requestMatchers("/api/users/{userId}/roles").permitAll() // Assign roles to user requires ADMIN authority
                .anyRequest().permitAll() // All other requests require ADMIN authority
            )
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Custom CORS configuration
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");  // Allow all origins (restrict as needed)
        configuration.addAllowedMethod("*");  // Allow all HTTP methods
        configuration.addAllowedHeader("*");  // Allow all headers
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);  // Apply CORS to all routes
        return source;
    }
}