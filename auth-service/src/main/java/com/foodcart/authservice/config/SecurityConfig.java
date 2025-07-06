// SecurityConfig: Spring Security configuration for JWT-based authentication and authorization

package com.foodcart.authservice.config;

import com.foodcart.authservice.security.JwtAuthConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configures Spring Security for:
 * - State less sessions
 * - OAuth2 JWT resource server
 * - Role extraction from JWT
 */
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthConverter jwtAuthConverter;

    /**
     * Defines the security filter chain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
    	http
    	
        // Disable CSRF since we use token-based (not cookie) authentication
    	.csrf(csrf -> csrf.disable())
        
        // No HTTP sessions created or used (stateless REST API)
    	.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        
        // Define access rules for different endpoints
    	.authorizeHttpRequests(auth -> auth
        
                // Allow unauthenticated access to auth endpoints and Swagger UI
    			.requestMatchers("/auth/register", "/auth/login", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                
                // Require authentication for all other requests
    			.anyRequest().authenticated()
            )
            
        // Enable OAuth2 resource server with JWT token support
    	.oauth2ResourceServer(oauth2 -> oauth2
        
                // Use custom converter to extract Keycloak roles from token
    			.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            );

        return http.build();
    }

    /**
     * Links custom JwtAuthConverter to Spring Security’s JWT converter
     * - Converts Key-cloak "realm_access.roles" → GrantedAuthorities
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwtAuthConverter);
        return converter;
    }
}
