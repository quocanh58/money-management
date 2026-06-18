package com.quocanhit.moneymanagement.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quocanhit.moneymanagement.payload.response.auth.AuthResponse;
import com.quocanhit.moneymanagement.service.implement.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        return path.equals("/api/v1/status")
                || path.equals("/api/v1/health")
                || path.equals("/api/v1/active")
                || path.equals("/api/v1/register")
                || path.equals("/api/v1/login")
                || path.equals("/api/v1/refresh-token")
                || path.equals("/error");
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if (request.getRequestURL().toString().endsWith("/refresh-token")) {
                filterChain.doFilter(request, response); // Tiếp tục mà không kiểm tra JWT
                return;
            }

            String token = extractToken(request);
            if (token != null) {
                jwtService.validateToken(token);

                String username = jwtService.extractClaims(token).getSubject();
                if (username != null) {
                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(username, null, null));
                }

            }

            filterChain.doFilter(request, response);
        } catch (RuntimeException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            sendErrorResponse(response, "Unauthorized: " + e.getMessage());
        }
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        AuthResponse<Object> userResponse = new AuthResponse<>(HttpStatus.UNAUTHORIZED, message, null);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter writer = response.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(userResponse));
        writer.flush();
    }

}
