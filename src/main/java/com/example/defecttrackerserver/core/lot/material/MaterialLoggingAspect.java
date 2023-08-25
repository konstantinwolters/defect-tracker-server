package com.example.defecttrackerserver.core.lot.material;

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
public class MaterialLoggingAspect {
    private final SecurityService securityService;

    @Around("execution(* com.example.defecttrackerserver.core.lot.material.MaterialService.saveMaterial(..))")
    public Object logSaveMaterial(ProceedingJoinPoint joinPoint) throws Throwable {
        return logSave(joinPoint);
    }

    @Around("execution(* com.example.defecttrackerserver.core.lot.material.MaterialService.updateMaterial(..))")
    public Object logUpdateMaterial(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMaterial("updateMaterial", joinPoint);
    }

    @Around("execution(* com.example.defecttrackerserver.core.lot.material.MaterialService.deleteMaterial(..))")
    public Object logDeleteDefectType(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMaterial("deleteMaterial", joinPoint);
    }

    private Object logMaterial(String material, ProceedingJoinPoint joinPoint) throws Throwable {
        Integer userId = securityService.getUser().getId();
        log.info("User {} called {} with params: {}", userId, material, Arrays.toString(joinPoint.getArgs()));

        long startTime = System.currentTimeMillis();
        Object retVal = joinPoint.proceed();
        long totalTime = System.currentTimeMillis() - startTime;

        log.info("User {} successfully executed {} in {} ms", userId, material, totalTime);
        return retVal;
    }

    private Object logSave(ProceedingJoinPoint joinPoint) throws Throwable {
        Integer userId = securityService.getUser().getId();
        log.info("User {} is trying to save an object with params: {}", userId, Arrays.toString(joinPoint.getArgs()));

        long startTime = System.currentTimeMillis();
        MaterialDto savedMaterialDto = (MaterialDto) joinPoint.proceed();
        long totalTime = System.currentTimeMillis() - startTime;

        log.info("User {} successfully saved an object with id {} in {} ms", userId, savedMaterialDto.getId(), totalTime);
        return savedMaterialDto;
    }

}
