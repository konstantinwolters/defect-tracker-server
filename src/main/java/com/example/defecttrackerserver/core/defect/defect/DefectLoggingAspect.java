package com.example.defecttrackerserver.core.defect.defect;

import com.example.defecttrackerserver.security.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Logs all mutating operations on Defects.
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
class DefectLoggingAspect {
    private final SecurityService securityService;

    @Around("execution(* com.example.defecttrackerserver.core.defect.defect.DefectService.saveDefect(..))")
    public Object logSaveDefect(ProceedingJoinPoint joinPoint) throws Throwable {
        return logSave(joinPoint);
    }

    @Around("execution(* com.example.defecttrackerserver.core.defect.defect.DefectService.updateDefect(..))")
    public Object logUpdateDefect(ProceedingJoinPoint joinPoint) throws Throwable {
        return logDefect("updateDefect", joinPoint);
    }

    @Around("execution(* com.example.defecttrackerserver.core.defect.defect.DefectService.deleteDefect(..))")
    public Object logDeleteAction(ProceedingJoinPoint joinPoint) throws Throwable {
        return logDefect("deleteDefect", joinPoint);
    }

    private Object logDefect(String defect, ProceedingJoinPoint joinPoint) throws Throwable {
        Integer userId = securityService.getUser().getId();
        log.info("User {} called {} with params: {}", userId, defect, Arrays.toString(joinPoint.getArgs()));

        long startTime = System.currentTimeMillis();
        Object retVal = joinPoint.proceed();
        long totalTime = System.currentTimeMillis() - startTime;

        log.info("User {} successfully executed {} in {} ms", userId, defect, totalTime);
        return retVal;
    }

    private Object logSave(ProceedingJoinPoint joinPoint) throws Throwable {
        Integer userId = securityService.getUser().getId();
        log.info("User {} is trying to save an object with params: {}", userId, Arrays.toString(joinPoint.getArgs()));

        long startTime = System.currentTimeMillis();
        DefectDto savedDefectDto = (DefectDto) joinPoint.proceed();
        long totalTime = System.currentTimeMillis() - startTime;

        log.info("User {} successfully saved an object with id {} in {} ms", userId, savedDefectDto.getId(), totalTime);
        return savedDefectDto;
    }
}
