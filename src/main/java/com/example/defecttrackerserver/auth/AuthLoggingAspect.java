package com.example.defecttrackerserver.auth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Aspect that logs successful and unsuccessful authentications.
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
class AuthLoggingAspect {

    @AfterReturning(
            pointcut = "execution(* com.example.defecttrackerserver.auth.AuthenticationService.authenticate(..))",
            returning = "result")
    public void afterReturningAuthenticate(JoinPoint joinPoint, Object result) {
        AuthenticationRequest request = (AuthenticationRequest) joinPoint.getArgs()[0];
        log.info("User: {} authenticated", request.getUsername());
    }

    @AfterThrowing(
            pointcut = "execution(* com.example.defecttrackerserver.auth.AuthenticationService.authenticate(..))",
            throwing = "exception")
    public void afterThrowingAuthenticate(JoinPoint joinPoint, Throwable exception) {
        AuthenticationRequest request = (AuthenticationRequest) joinPoint.getArgs()[0];
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attrs != null) {
            HttpServletRequest httpServletRequest = attrs.getRequest();
            String clientIP = getClientIP(httpServletRequest);

            log.warn("Error authenticating for user: {}, reason: {}, IP: {}", request.getUsername(), exception.getMessage(), clientIP);
        } else {
            log.warn("Error authenticating for user: {}, reason: {}", request.getUsername(), exception.getMessage());
        }
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0]; // For cases when behind a proxy
    }
}
