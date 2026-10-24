
package com.bookstore.demo.config;

import com.bookstore.demo.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http

                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/api/users/register",
                                "/api/auth/login"
                        ).permitAll()

                        .requestMatchers("/api/users/me", "/api/users/me/**")
                        .hasAnyRole("USER", "ADMIN")

                        .requestMatchers("/api/cart", "/api/cart/**")
                        .hasAnyRole("USER", "ADMIN")

                        .requestMatchers("/api/orders", "/api/orders/**")
                        .hasAnyRole("USER", "ADMIN")

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/books",
                                "/api/books/**"
                        )
                        .hasAnyRole("USER", "ADMIN")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/books"
                        )
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/books/**"
                        )
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/books/**"
                        )
                        .hasRole("ADMIN")

                        .requestMatchers("/api/admins/**")
                        .hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
