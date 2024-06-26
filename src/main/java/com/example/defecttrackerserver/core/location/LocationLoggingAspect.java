package com.example.defecttrackerserver.core.location;

import com.example.defecttrackerserver.security.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Aspect that logs all mutating operations on Locations.
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
class LocationLoggingAspect {
    private final SecurityService securityService;

    @Around("execution(* com.example.defecttrackerserver.core.location.LocationService.saveLocation(..))")
    public Object logSaveLocation(ProceedingJoinPoint joinPoint) throws Throwable {
        return logSave(joinPoint);
    }

    @Around("execution(* com.example.defecttrackerserver.core.location.LocationService.updateLocation(..))")
    public Object logUpdateLocation(ProceedingJoinPoint joinPoint) throws Throwable {
        return logLocation("updateLocation", joinPoint);
    }

    @Around("execution(* com.example.defecttrackerserver.core.location.LocationService.deleteLocation(..))")
    public Object logDeleteLocation(ProceedingJoinPoint joinPoint) throws Throwable {
        return logLocation("deleteLocation", joinPoint);
    }

    private Object logLocation(String location, ProceedingJoinPoint joinPoint) throws Throwable {
        Integer userId = securityService.getUser().getId();
        log.info("User {} called {} with params: {}", userId, location, Arrays.toString(joinPoint.getArgs()));

        long startTime = System.currentTimeMillis();
        Object retVal = joinPoint.proceed();
        long totalTime = System.currentTimeMillis() - startTime;

        log.info("User {} successfully executed {} in {} ms", userId, location, totalTime);
        return retVal;
    }

    private Object logSave(ProceedingJoinPoint joinPoint) throws Throwable {
        Integer userId = securityService.getUser().getId();
        log.info("User {} is trying to save an object with params: {}", userId, Arrays.toString(joinPoint.getArgs()));

        long startTime = System.currentTimeMillis();
        LocationDto savedLocationDto = (LocationDto) joinPoint.proceed();
        long totalTime = System.currentTimeMillis() - startTime;

        log.info("User {} successfully saved an object with id {} in {} ms", userId, savedLocationDto.getId(), totalTime);
        return savedLocationDto;
    }
}
