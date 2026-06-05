package com.mprribeiro.app_ai_crafter.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static java.util.Objects.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AuthUtil authUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("Processing JWT authentication for request: {}", request.getRequestURI());

        final String requestHeaderToken = request.getHeader("Authorization");

        if (isNull(requestHeaderToken) || !requestHeaderToken.startsWith("Bearer ")) {
            log.warn("Invalid JWT token format");
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = requestHeaderToken.substring(7);

        final var jwtUserPrincipal = authUtil.verifyToken(jwtToken);
        if (nonNull(jwtUserPrincipal) &&
                isNull(SecurityContextHolder.getContext().getAuthentication())) {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    jwtUserPrincipal, null, jwtUserPrincipal.authorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }
}
