package com.example.defecttrackerserver.core.defect.defectComment;

import com.example.defecttrackerserver.security.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Aspect that logs all mutating operations on Defect Comments.
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
class DefectCommentLoggingAspect {
    private final SecurityService securityService;

    @Around("execution(* com.example.defecttrackerserver.core.defect.defectComment.DefectCommentService.addDefectCommentToDefect(..))")
    public Object logSaveDefectComment(ProceedingJoinPoint joinPoint) throws Throwable {
        return logSave(joinPoint);
    }

    @Around("execution(* com.example.defecttrackerserver.core.defect.defectComment.DefectCommentService.updateDefectComment(..))")
    public Object logUpdateDefectComment(ProceedingJoinPoint joinPoint) throws Throwable {
        return logDefectComment("updateDefectComment", joinPoint);
    }

    @Around("execution(* com.example.defecttrackerserver.core.defect.defectComment.DefectCommentService.deleteDefectComment(..))")
    public Object logDeleteDefectComment(ProceedingJoinPoint joinPoint) throws Throwable {
        return logDefectComment("deleteDefectComment", joinPoint);
    }

    private Object logDefectComment(String defectComment, ProceedingJoinPoint joinPoint) throws Throwable {
        Integer userId = securityService.getUser().getId();
        log.info("User {} called {} with params: {}", userId, defectComment, Arrays.toString(joinPoint.getArgs()));

        long startTime = System.currentTimeMillis();
        Object retVal = joinPoint.proceed();
        long totalTime = System.currentTimeMillis() - startTime;

        log.info("User {} successfully executed {} in {} ms", userId, defectComment, totalTime);
        return retVal;
    }

    private Object logSave(ProceedingJoinPoint joinPoint) throws Throwable {
        Integer userId = securityService.getUser().getId();
        log.info("User {} is trying to save an object with params: {}", userId, Arrays.toString(joinPoint.getArgs()));

        long startTime = System.currentTimeMillis();
        DefectCommentDto savedDefectCommentDto = (DefectCommentDto) joinPoint.proceed();
        long totalTime = System.currentTimeMillis() - startTime;

        log.info("User {} successfully saved an object with id {} in {} ms", userId, savedDefectCommentDto.getId(), totalTime);
        return savedDefectCommentDto;
    }
}
