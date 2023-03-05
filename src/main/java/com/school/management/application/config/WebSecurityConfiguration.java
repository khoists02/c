package com.school.management.application.config;

import com.school.management.application.api.auth.UnauthenticatedHandler;
import com.school.management.application.api.auth.UnauthoriseHandler;
import com.school.management.application.api.filter.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.ForwardedHeaderFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Autowired
    private UnauthenticatedHandler unauthenticatedHandler;
    @Autowired
    private UnauthoriseHandler  unauthoriseHandler;

    @Bean
    ForwardedHeaderFilter forwardedHeaderFilter() { return new ForwardedHeaderFilter(); }

    @Bean
    public SecurityFilterChain httpFilterChain(HttpSecurity http) throws Exception {
        /***
         * Extends from onceperrequest => run one by one !!!
         * */
//        http.addFilterAfter(beanFactory.createBean(FilterChainExceptionHandler.class), CsrfFilter.class); // CsrfFilter run first => FilterChainExceptionHandler run seconds
//        http.addFilterAfter(beanFactory.createBean(CookieAuthFilter.class), FilterChainExceptionHandler.class); // AuthorizationFilter run first => CookieAuthFilter Run seconds
//        http.addFilterAfter(beanFactory.createBean(FilterChainExceptionHandler.class), AuthorizationFilter.class); // FilterChainExceptionHandler run first => CookieAuthFilter Run seconds
        http.addFilterAfter(beanFactory.createBean(CookieAuthFilter.class), CsrfFilter.class);
        http.addFilterAfter(beanFactory.createBean(FilterChainExceptionHandler.class), AuthorizationFilter.class);
        http.csrf().ignoringRequestMatchers("/api/v1/auth/**");
        http.exceptionHandling().authenticationEntryPoint(unauthenticatedHandler).accessDeniedHandler(unauthoriseHandler);
        http.authorizeHttpRequests().requestMatchers("/api/v1/auth/**").permitAll().anyRequest().authenticated();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();
    }


}
