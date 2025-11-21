package com.smart.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configure(http))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/login").permitAll()
                //.requestMatchers("/admin/**").permitAll() // Remove this to enforce auth
                .requestMatchers("/app/**").permitAll()
                .anyRequest().authenticated()
            );
        http.addFilterBefore(jwtAuthenticationTokenFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @org.springframework.beans.factory.annotation.Autowired
    private com.smart.system.security.filter.JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
}
