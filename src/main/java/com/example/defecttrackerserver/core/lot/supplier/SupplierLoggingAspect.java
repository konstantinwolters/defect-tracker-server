package com.example.defecttrackerserver.core.lot.supplier;

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
public class SupplierLoggingAspect {
    private final SecurityService securityService;

    @Around("execution(* com.example.defecttrackerserver.core.lot.supplier.SupplierService.updateSupplier(..))")
    public Object logUpdateSupplier(ProceedingJoinPoint joinPoint) throws Throwable {
        return logSupplier("updateSupplier", joinPoint);
    }

    @Around("execution(* com.example.defecttrackerserver.core.lot.supplier.SupplierService.deleteSupplier(..))")
    public Object logDeleteDefectType(ProceedingJoinPoint joinPoint) throws Throwable {
        return logSupplier("deleteSupplier", joinPoint);
    }

    private Object logSupplier(String supplier, ProceedingJoinPoint joinPoint) throws Throwable {
        Integer userId = securityService.getUser().getId();
        log.info("User {} called {} with params: {}", userId, supplier, Arrays.toString(joinPoint.getArgs()));

        long startTime = System.currentTimeMillis();
        Object retVal = joinPoint.proceed();
        long totalTime = System.currentTimeMillis() - startTime;

        log.info("User {} successfully executed {} in {} ms", userId, supplier, totalTime);
        return retVal;
    }
}