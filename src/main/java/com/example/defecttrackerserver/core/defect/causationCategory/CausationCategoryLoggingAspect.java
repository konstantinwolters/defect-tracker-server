package com.example.defecttrackerserver.core.defect.causationCategory;

import com.example.defecttrackerserver.security.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Logging aspect that logs all mutating operations on Causation Categories.
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
class CausationCategoryLoggingAspect {
    private final SecurityService securityService;

    @Around("execution(* com.example.defecttrackerserver.core.defect.causationCategory.CausationCategoryService.saveCausationCategory(..))")
    public Object logUpdateCausationCategory(ProceedingJoinPoint joinPoint) throws Throwable {
        return logSave(joinPoint);
    }

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

    private Object logSave(ProceedingJoinPoint joinPoint) throws Throwable {
        Integer userId = securityService.getUser().getId();
        log.info("User {} is trying to save an object with params: {}", userId, Arrays.toString(joinPoint.getArgs()));

        long startTime = System.currentTimeMillis();
        CausationCategoryDto savedCausationCategoryDto = (CausationCategoryDto) joinPoint.proceed();
        long totalTime = System.currentTimeMillis() - startTime;

        log.info("User {} successfully saved an object with id {} in {} ms", userId, savedCausationCategoryDto.getId(), totalTime);
        return savedCausationCategoryDto;
    }
}
