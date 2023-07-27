package com.example.defecttrackerserver.core.lot.lot;

import com.example.defecttrackerserver.security.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class LotLoggingAspect {
    private final SecurityService securityService;

    @Around("execution(* com.example.defecttrackerserver.core.lot.lot.LotService.updateLot(..))")
    public Object logUpdateLot(ProceedingJoinPoint joinPoint) throws Throwable {
        return logLot("updateLot", joinPoint);
    }

    @Around("execution(* com.example.defecttrackerserver.core.lot.lot.LotService.deleteLot(..))")
    public Object logDeleteDefectType(ProceedingJoinPoint joinPoint) throws Throwable {
        return logLot("deleteLot", joinPoint);
    }

    private Object logLot(String lot, ProceedingJoinPoint joinPoint) throws Throwable {
        Integer userId = securityService.getUser().getId();
        log.info("User {} called {} with params: {}", userId, lot, Arrays.toString(joinPoint.getArgs()));

        long startTime = System.currentTimeMillis();
        Object retVal = joinPoint.proceed();
        long totalTime = System.currentTimeMillis() - startTime;

        log.info("User {} successfully executed {} in {} ms", userId, lot, totalTime);
        return retVal;
    }
}
