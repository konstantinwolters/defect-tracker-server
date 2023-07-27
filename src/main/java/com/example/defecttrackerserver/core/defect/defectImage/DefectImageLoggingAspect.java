package com.example.defecttrackerserver.core.defect.defectImage;

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
public class DefectImageLoggingAspect {
    private final SecurityService securityService;

    @Around("execution(* com.example.defecttrackerserver.core.defect.defectImage.DefectImageService.updateDefectImage(..))")
    public Object logUpdateDefectImage(ProceedingJoinPoint joinPoint) throws Throwable {
        return logDefectImage("updateDefectImage", joinPoint);
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
}
