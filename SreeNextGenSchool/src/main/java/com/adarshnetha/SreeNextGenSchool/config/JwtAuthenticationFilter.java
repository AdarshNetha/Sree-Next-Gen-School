package com.adarshnetha.SreeNextGenSchool.config;

import com.adarshnetha.SreeNextGenSchool.dto.ResponseStructure;
import com.adarshnetha.SreeNextGenSchool.exception.TokenExpiredException;
import com.adarshnetha.SreeNextGenSchool.service.AdminUserDetailsService;
import com.adarshnetha.SreeNextGenSchool.service.JwtTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final JwtTokenService jwtTokenService;
    private final AdminUserDetailsService adminUserDetailsService;

    public JwtAuthenticationFilter(
            JwtTokenService jwtTokenService,
            AdminUserDetailsService adminUserDetailsService
    ) {
        this.jwtTokenService = jwtTokenService;
        this.adminUserDetailsService = adminUserDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = jwtTokenService.extractToken(authorizationHeader);
            String username = jwtTokenService.validateAndGetUsername(token);
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = adminUserDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (TokenExpiredException exception) {
            SecurityContextHolder.clearContext();
            writeUnauthorizedResponse(response, exception.getMessage());
        }
    }

    private void writeUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        ResponseStructure<Object> responseStructure = new ResponseStructure<>(
                message,
                null,
                HttpStatus.UNAUTHORIZED.value()
        );
        response.getWriter().write(OBJECT_MAPPER.writeValueAsString(responseStructure));
    }
}
