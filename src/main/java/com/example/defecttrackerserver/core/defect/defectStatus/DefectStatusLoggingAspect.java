package com.example.defecttrackerserver.core.defect.defectStatus;

import com.example.defecttrackerserver.core.action.ActionDto;
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
public class DefectStatusLoggingAspect {
    private final SecurityService securityService;

    @Around("execution(* com.example.defecttrackerserver.core.defect.defectStatus.DefectStatusService.saveDefectStatus(..))")
    public Object logSaveDefectImage(ProceedingJoinPoint joinPoint) throws Throwable {
        return logSave(joinPoint);
    }

    @Around("execution(* com.example.defecttrackerserver.core.defect.defectStatus.DefectStatusService.updateDefectStatus(..))")
    public Object logUpdateDefectImage(ProceedingJoinPoint joinPoint) throws Throwable {
        return logDefectStatus("updateDefectStatus", joinPoint);
    }

    @Around("execution(* com.example.defecttrackerserver.core.defect.defectStatus.DefectStatusService.deleteDefectStatus(..))")
    public Object logDeleteDefectImage(ProceedingJoinPoint joinPoint) throws Throwable {
        return logDefectStatus("deleteDefectStatus", joinPoint);
    }

    private Object logDefectStatus(String defectStatus, ProceedingJoinPoint joinPoint) throws Throwable {
        Integer userId = securityService.getUser().getId();
        log.info("User {} called {} with params: {}", userId, defectStatus, Arrays.toString(joinPoint.getArgs()));

        long startTime = System.currentTimeMillis();
        Object retVal = joinPoint.proceed();
        long totalTime = System.currentTimeMillis() - startTime;

        log.info("User {} successfully executed {} in {} ms", userId, defectStatus, totalTime);
        return retVal;
    }

    private Object logSave(ProceedingJoinPoint joinPoint) throws Throwable {
        Integer userId = securityService.getUser().getId();
        log.info("User {} is trying to save an object with params: {}", userId, Arrays.toString(joinPoint.getArgs()));

        long startTime = System.currentTimeMillis();
        DefectStatusDto savedDefectStatusDto = (DefectStatusDto) joinPoint.proceed();
        long totalTime = System.currentTimeMillis() - startTime;

        log.info("User {} successfully saved an object with id {} in {} ms", userId, savedDefectStatusDto.getId(), totalTime);
        return savedDefectStatusDto;
    }
}
