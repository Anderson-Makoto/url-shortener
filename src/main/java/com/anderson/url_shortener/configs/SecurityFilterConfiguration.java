package com.anderson.url_shortener.configs;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.anderson.url_shortener.entities.UserEntity;
import com.anderson.url_shortener.repositories.UserRepository;
import com.anderson.url_shortener.services.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilterConfiguration extends OncePerRequestFilter {

    @Autowired
    JwtTokenService jwtTokenService;

    @Autowired
    UserRepository userRepository;

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        if ("/api/user/login".equals(path) || "/api/user/save".equals(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        var token = this.recoverToken(request);
        if (token != null) {
            var subject = jwtTokenService.validateToken(token);
            UserEntity userEntity = userRepository.findByEmail(subject);

            var authentication = new UsernamePasswordAuthenticationToken(userEntity, null, userEntity.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return null;
        }

        return authHeader.replace("Bearer ", "");
    }

}
