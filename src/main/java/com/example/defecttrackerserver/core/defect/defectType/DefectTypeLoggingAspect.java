package com.example.defecttrackerserver.core.defect.defectType;

import com.example.defecttrackerserver.core.defect.defectStatus.DefectStatusDto;
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
public class DefectTypeLoggingAspect {
    private final SecurityService securityService;

    @Around("execution(* com.example.defecttrackerserver.core.defect.defectType.DefectTypeService.saveDefectType(..))")
    public Object logSaveDefectType(ProceedingJoinPoint joinPoint) throws Throwable {
        return logSave(joinPoint);
    }

    @Around("execution(* com.example.defecttrackerserver.core.defect.defectType.DefectTypeService.updateDefectType(..))")
    public Object logUpdateDefectType(ProceedingJoinPoint joinPoint) throws Throwable {
        return logDefectType("updateDefectType", joinPoint);
    }

    @Around("execution(* com.example.defecttrackerserver.core.defect.defectType.DefectTypeService.deleteDefectType(..))")
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
        DefectTypeDto savedDefectTypeDto = (DefectTypeDto) joinPoint.proceed();
        long totalTime = System.currentTimeMillis() - startTime;

        log.info("User {} successfully saved an object with id {} in {} ms", userId, savedDefectTypeDto.getId(), totalTime);
        return savedDefectTypeDto;
    }
}
