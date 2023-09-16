package com.example.defecttrackerserver.core.defect.defectImage;

import com.example.defecttrackerserver.security.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Aspect that logs all mutating operations on Defect Images.
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DefectImageLoggingAspect {
    private final SecurityService securityService;

    @Around("execution(* com.example.defecttrackerserver.core.defect.defectImage.DefectImageService.saveDefectImageToDefect(..))")
    public Object logSaveDefectImage(ProceedingJoinPoint joinPoint) throws Throwable {
        return logSave(joinPoint);
    }

    @Around("execution(* com.example.defecttrackerserver.core.defect.defectImage.DefectImageService.deleteDefectImage(..))")
    public Object logDeleteDefectImage(ProceedingJoinPoint joinPoint) throws Throwable {
        return logDefectImage("deleteDefectImage", joinPoint);
    }

    private Object logDefectImage(String defectImage, ProceedingJoinPoint joinPoint) throws Throwable {
        Integer userId = securityService.getUser().getId();
        log.info("User {} called {} with params: {}", userId, defectImage, Arrays.toString(joinPoint.getArgs()));

        long startTime = System.currentTimeMillis();
        Object retVal = joinPoint.proceed();
        long totalTime = System.currentTimeMillis() - startTime;

        log.info("User {} successfully executed {} in {} ms", userId, defectImage, totalTime);
        return retVal;
    }

    private Object logSave(ProceedingJoinPoint joinPoint) throws Throwable {
        Integer userId = securityService.getUser().getId();
        log.info("User {} is trying to save an object with params: {}", userId, Arrays.toString(joinPoint.getArgs()));

        long startTime = System.currentTimeMillis();
        DefectImageDto savedDefectImageDto = (DefectImageDto) joinPoint.proceed();
        long totalTime = System.currentTimeMillis() - startTime;

        log.info("User {} successfully saved an object with id {} in {} ms", userId, savedDefectImageDto.getId(), totalTime);
        return savedDefectImageDto;
    }
}
