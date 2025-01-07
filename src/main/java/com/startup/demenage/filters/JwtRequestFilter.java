package com.startup.demenage.filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.startup.demenage.security.JwtUtil;
import com.startup.demenage.utils.RoleHolder;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class JwtRequestFilter extends OncePerRequestFilter {

    // private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RoleHolder roleHolder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                DecodedJWT jwt = jwtUtil.decodeToken(jwtToken);
                String role = jwtUtil.extractRole(jwt);
                roleHolder.setRole(role);
            } catch (Exception e) {
                // logger.error("JWT Token is invalid: {}", e.getMessage());
            }
        } else {
            // logger.warn("JWT Token does not begin with Bearer String");
        }
        chain.doFilter(request, response);
    }
}

