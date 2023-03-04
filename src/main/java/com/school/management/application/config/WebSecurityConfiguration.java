package com.school.management.application.config;

import com.school.management.application.api.filter.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {
    private final CookieAuthFilter cookieAuthFilter;
    private final AuthorizeFilter authorizeFilter;
    private final FilterChainExceptionHandler filterChainExceptionHandler;

    @Bean
    public SecurityFilterChain httpFilterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(filterChainExceptionHandler, UsernamePasswordAuthenticationFilter.class); // Filter Before => Filter
        http.addFilterBefore(cookieAuthFilter, FilterChainExceptionHandler.class);
        http.addFilterBefore(authorizeFilter, FilterChainExceptionHandler.class);
        http.csrf().ignoringRequestMatchers("/api/v1/auth/**");
        http.authorizeHttpRequests().requestMatchers("/api/v1/auth/**").permitAll().anyRequest().authenticated();
        http.authorizeHttpRequests();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();
    }


}
