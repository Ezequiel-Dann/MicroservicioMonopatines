package main.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import main.app.security.ExternalTokenAuthenticationFilter;


@Configuration
public class SecurityConfig {

    private final ExternalTokenAuthenticationFilter externalTokenAuthenticationFilter;

    public SecurityConfig(ExternalTokenAuthenticationFilter externalTokenAuthenticationFilter) {
        this.externalTokenAuthenticationFilter = externalTokenAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                     //   		"**",
                     //   		"/**",
                        		 "/api-docs/**",         // OpenAPI specification path
                                 "/swagger-ui.html",     // Swagger UI HTML page
                                 "/swagger-ui/**" 
                                 /*
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html/**",
                                "/openapi/swagger-ui/**",
                                "/openapi/monopatin-api.yaml"
                                */
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST, "/monopatines/").hasRole("ADMIN")
                        //.anyRequest().hasAnyRole("USER", "ADMIN")
                        .anyRequest().hasAnyRole("USER")
                )
                .addFilterBefore(externalTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


}