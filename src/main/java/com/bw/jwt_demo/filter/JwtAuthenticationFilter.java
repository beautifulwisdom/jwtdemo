package com.bw.jwt_demo.filter;

import com.bw.jwt_demo.service.CustomUserDetailsService;
import com.bw.jwt_demo.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader("Authorization");
        String userName = null;
        String token = null;

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
            try {
                userName = jwtUtil.extractUsername(token);
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);

                if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(userDetails,
                            null, userDetails.getAuthorities());
                    upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(upat);
                } else {
                    System.out.println("Invalid token");
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid Bearer Token format");
        }

        filterChain.doFilter(request, response);
    }
}
