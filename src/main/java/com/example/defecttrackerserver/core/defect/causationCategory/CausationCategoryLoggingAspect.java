package com.example.defecttrackerserver.core.defect.causationCategory;

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
public class CausationCategoryLoggingAspect {
    private final SecurityService securityService;

    @Around("execution(* com.example.defecttrackerserver.core.defect.causationCategory.CausationCategoryService.updateCausationCategory(..))")
    public Object logUpdateDefectType(ProceedingJoinPoint joinPoint) throws Throwable {
        return logDefectType("updateDefectType", joinPoint);
    }

    @Around("execution(* com.example.defecttrackerserver.core.defect.causationCategory.CausationCategoryService.deleteCausationCategory(..))")
    public Object logDeleteDefectType(ProceedingJoinPoint joinPoint) throws Throwable {
        return logDefectType("deleteDefectType", joinPoint);
    }

    private Object logDefectType(String defectType, ProceedingJoinPoint joinPoint) throws Throwable {
        Integer userId = securityService.getUser().getId();
        log.info("User {} called {} with params: {}", userId, defectType, Arrays.toString(joinPoint.getArgs()));

        long startTime = System.currentTimeMillis();
        Object retVal = joinPoint.proceed();
        long totalTime = System.currentTimeMillis() - startTime;

        log.info("User {} successfully executed {} in {} ms", userId, defectType, totalTime);
        return retVal;
    }
}