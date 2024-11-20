package com.lab9_10.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Bật/tắt CSRF protection
                .csrf(csrf -> csrf.disable())

                // Cấu hình xác thực/ủy quyền
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**", "/api/account/**").permitAll() // Các endpoint công khai
                        .anyRequest().authenticated() // Các endpoint khác cần xác thực
                )

                // Cho phép dùng HTTP Basic Auth
                .httpBasic(Customizer.withDefaults())

                // Cho phép sử dụng frame cho H2 Console
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

