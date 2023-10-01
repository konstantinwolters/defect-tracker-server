package com.example.defecttrackerserver.notification;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.action.ActionDto;
import com.example.defecttrackerserver.core.action.ActionRepository;
import com.example.defecttrackerserver.core.defect.defect.Defect;
import com.example.defecttrackerserver.core.defect.defect.DefectDto;
import com.example.defecttrackerserver.core.lot.lot.Lot;
import com.example.defecttrackerserver.core.lot.lot.LotRepository;
import com.example.defecttrackerserver.core.user.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Aspect that sends email notifications for different methods.
 */
@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class NotificationAspect {
    private final EmailService emailService;
    private final MessageSource messageSource;
    private final LotRepository lotRepository;
    private final ActionRepository actionRepository;

    @AfterReturning(value = "@annotation(com.example.defecttrackerserver.notification.NotifyUsers)", returning = "result")
    public void afterActionCreated(JoinPoint joinPoint, Object result) {
        Locale currentLocale = LocaleContextHolder.getLocale();

        String[] recipients = new String[0];
        String subject = "";
        String body = "";

        if (result instanceof DefectDto defect) {

            Lot lot = lotRepository.findByLotNumber(defect.getLot()).orElseThrow(
                    () -> new EntityNotFoundException("Lot does not exist" +
                    "with LotNumber:" + defect.getLot()));

            recipients = lot.getMaterial().getResponsibleUsers().stream()
                    .map(User::getMail)
                    .toArray(String[]::new);

            String subjectTemplate = messageSource.getMessage("email.subject.newDefect", null, currentLocale);
            subject = subjectTemplate.formatted(defect.getId());

            String bodyTemplate = messageSource.getMessage("email.body.newDefect", null, currentLocale);
            body = bodyTemplate.formatted(
                            defect.getId(),
                            defect.getDescription(),
                            defect.getProcess());

        } else if (result instanceof ActionDto action) {
            Action newAction = actionRepository.findById(action.getId()).orElseThrow(
                    () -> new EntityNotFoundException("Action does not exist with id: " + action.getId()));

            recipients = newAction.getAssignedUsers().stream()
                    .map(User::getMail)
                    .toArray(String[]::new);

            String subjectTemplate = messageSource.getMessage("email.subject.newAction", null, currentLocale);
            subject = subjectTemplate.formatted(action.getId());

            String bodyTemplate = messageSource.getMessage("email.body.newAction", null, currentLocale);
            body = bodyTemplate.formatted(
                    action.getId(),
                    action.getDescription(),
                    action.getDueDate());
        }

        List<String> failedRecipients = new ArrayList<>();
        for (String recipient : recipients) {
            try {
                emailService.sendSimpleEmail(recipient, subject, body);
            } catch (Exception e) {
                failedRecipients.add(recipient);
            }
        }

        if (!failedRecipients.isEmpty()) {
            log.error("Failed to send emails to recipients: {}", String.join(", ", failedRecipients));
        }
    }
}
