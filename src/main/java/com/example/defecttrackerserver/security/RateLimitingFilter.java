package com.example.defecttrackerserver.security;

import com.example.defecttrackerserver.exception.customExceptions.MaxConcurrentRequestsExceededException;
import com.example.defecttrackerserver.exception.customExceptions.MaxUserRequestExceededException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RateLimitingFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final BucketService bucketService;
    private Semaphore semaphore;

    // Concurrent request limit
    @Value("${MAX-CONCURRENT-REQUESTS}")
    private int MAX_CONCURRENT_REQUESTS;

    @PostConstruct
    public void init() {
        semaphore = new Semaphore(MAX_CONCURRENT_REQUESTS);
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        final String username;
        response.setContentType("application/json");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = authHeader.substring(7);
        username = jwtService.getUsernameFromToken(jwtToken);
        
        if (username == null)
            throw new IllegalArgumentException("Username missing.");

        if(!bucketService.isTokenBucketEmpty(username)) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            String responseBody = "{ \"message\": \"" +
                    "User rate limit exceeded. Please try again later."
                    + "\", \"status\": 429 }";
            response.getWriter().write(responseBody);
            return;
        }

        try {
            // Limit concurrent requests
            if (!semaphore.tryAcquire(500, TimeUnit.MILLISECONDS)){
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                String responseBody = "{ \"message\": \"" +
                        "Concurrent rate limit exceeded. Please try again later."
                        + "\", \"status\": 429 }";
                response.getWriter().write(responseBody);
                return;
            }

            filterChain.doFilter(request, response);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            
        } finally {
            semaphore.release();
        }
    }
}