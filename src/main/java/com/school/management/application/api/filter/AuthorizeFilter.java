package com.school.management.application.api.filter;

import com.school.management.application.exceptions.UnauthenticatedException;
import com.school.management.application.model.Role;
import com.school.management.application.repositories.UsersRepository;
import com.school.management.application.utils.UserUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthorizeFilter extends OncePerRequestFilter {

    @Autowired
    private UsersRepository usersRepository;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return antPathMatcher.match("/api/v1/auth/**", request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        UUID userId = UserUtils.getUser().getId();
        System.out.println(userId);
        try {
            boolean isValid = usersRepository.hasPermission(UserUtils.getUser(), "viewstudy");
            if (!isValid) {
                throw UnauthenticatedException.MALFORMED_TOKEN;
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            throw UnauthenticatedException.MALFORMED_TOKEN;
        }
    }
}
