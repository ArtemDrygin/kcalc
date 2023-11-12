package com.putanyname.kcalc.config.security;

import com.putanyname.kcalc.entity.SettingsEntity;
import com.putanyname.kcalc.repository.SettingsEntityRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostLoginFilter extends OncePerRequestFilter {
    private final SettingsEntityRepository settingsEntityRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        var sub = authentication.getToken().getClaim("sub");
        var userId = UUID.fromString((String) sub);

        var isSettingsExist = settingsEntityRepository.existsByUserId(userId);
        if (!isSettingsExist) {
            var settingsEntity = new SettingsEntity();
            settingsEntity.setUserId(userId);
        }

        filterChain.doFilter(request, response);
    }
}
