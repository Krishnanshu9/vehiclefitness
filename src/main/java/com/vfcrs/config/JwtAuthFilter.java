package com.vfcrs.config;

import com.vfcrs.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private static final Logger logger = Logger.getLogger(JwtAuthFilter.class.getName());

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Extract Authorization header
        String authHeader = request.getHeader("Authorization");

        // Check if the header is present and starts with "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);  // Extract token from "Bearer <token>"
            try {
                String email = jwtUtils.extractEmail(token);  // Extract email from token

                // Validate token
                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Load user details based on the email from the token
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                    // Check if the token is valid
                    if (jwtUtils.validateToken(token)) {
                        // Create an authentication object and set it in the security context
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities());

                        // Add details to the authentication token
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // Set the authentication context for the request
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        logger.log(Level.FINE, "User authenticated: {0}", email);
                    }
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "Authentication error: {0}", e.getMessage());
            }
        }

        // Proceed with the request
        filterChain.doFilter(request, response);
    }
}
