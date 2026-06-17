package com.example.demo.config;

import com.example.demo.security.CustomDetailService;
import com.example.demo.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthentificationFilter extends OncePerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer ";
    private final JwtService jwtService;
    private final CustomDetailService customDetailService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Filter invoked!!!!!!!!!!!");
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(BEARER_PREFIX.length());

        try {
            String username = jwtService.extractName(token);
            boolean contextIsEmpty = SecurityContextHolder.getContext().getAuthentication() == null;

            if (username != null && contextIsEmpty) {
                UserDetails userDetails = customDetailService.loadUserByUsername(username);

                if (jwtService.isAccessTokenValid(token, userDetails)) {
                    var authorities = jwtService.extractRoles(token).stream().map(SimpleGrantedAuthority::new).toList();
                    var autentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

                    autentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(autentication);
                } else {
                    writeInvalidTokenResponse(response);
                    return;
                }
            }
        }catch (Exception e){
            logger.error("Invalid JWT",e);
            writeInvalidTokenResponse(response);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void writeInvalidTokenResponse(HttpServletResponse response) throws IOException {
    SecurityContextHolder.clearContext();
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");
    response.getWriter().write("""
            {
            "error" "invalid_token"
            }       
            """);
    }
}
