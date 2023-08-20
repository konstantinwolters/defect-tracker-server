package com.example.defecttrackerserver.core.action;

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
public class ActionLoggingAspect {
    private final SecurityService securityService;

    @Around("execution(* com.example.defecttrackerserver.core.action.ActionService.saveAction(..))")
    public Object logSaveAction(ProceedingJoinPoint joinPoint) throws Throwable {
        return logSave(joinPoint);
    }

    @Around("execution(* com.example.defecttrackerserver.core.action.ActionService.closeAction(..))")
    public Object logCloseAction(ProceedingJoinPoint joinPoint) throws Throwable {
        return logAction("closeAction", joinPoint);
    }

    @Around("execution(* com.example.defecttrackerserver.core.action.ActionService.updateAction(..))")
    public Object logUpdateAction(ProceedingJoinPoint joinPoint) throws Throwable {
        return logAction("updateAction", joinPoint);
    }

    @Around("execution(* com.example.defecttrackerserver.core.action.ActionService.deleteAction(..))")
    public Object logDeleteAction(ProceedingJoinPoint joinPoint) throws Throwable {
        return logAction("deleteAction", joinPoint);
    }

    private Object logAction(String action, ProceedingJoinPoint joinPoint) throws Throwable {
        Integer userId = securityService.getUser().getId();
        log.info("User {} called {} with params: {}", userId, action, Arrays.toString(joinPoint.getArgs()));

        long startTime = System.currentTimeMillis();
        Object retVal = joinPoint.proceed();
        long totalTime = System.currentTimeMillis() - startTime;

        log.info("User {} successfully executed {} in {} ms", userId, action, totalTime);
        return retVal;
    }

    private Object logSave(ProceedingJoinPoint joinPoint) throws Throwable {
        Integer userId = securityService.getUser().getId();
        log.info("User {} is trying to save an object with params: {}", userId, Arrays.toString(joinPoint.getArgs()));

        long startTime = System.currentTimeMillis();
        ActionDto savedActionDto = (ActionDto) joinPoint.proceed();
        long totalTime = System.currentTimeMillis() - startTime;

        log.info("User {} successfully saved an object with id {} in {} ms", userId, savedActionDto.getId(), totalTime);
        return savedActionDto;
    }
}
