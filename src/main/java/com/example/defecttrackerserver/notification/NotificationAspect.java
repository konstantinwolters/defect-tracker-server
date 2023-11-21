package com.example.defecttrackerserver.notification;

import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.action.ActionDto;
import com.example.defecttrackerserver.core.coreService.EntityService;
import com.example.defecttrackerserver.core.defect.defect.DefectDto;
import com.example.defecttrackerserver.core.lot.lot.Lot;
import com.example.defecttrackerserver.core.user.user.User;
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
import java.util.Set;


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
    private final EntityService entityService;

    @AfterReturning(value = "@annotation(com.example.defecttrackerserver.notification.NotifyUsers)", returning = "result")
    public void afterNotificationTriggered(JoinPoint joinPoint, Object result) {
        if (result instanceof DefectDto defect) {
            handleDefectNotification(defect);
        } else if (result instanceof ActionDto action) {
            handleActionNotification(action);
        }
    }

    void handleDefectNotification(DefectDto defect){
        Locale currentLocale = LocaleContextHolder.getLocale();
        Lot lot = entityService.getLotById(defect.getLot().getId());
        String[] recipients = fetchRecipientsEmailAddresses(lot.getMaterial().getResponsibleUsers());
        String subject = prepareSubject("email.subject.newDefect", defect.getId(), currentLocale);

        String body = prepareBody("email.body.newDefect",
                new Object[]{defect.getId(), defect.getDescription(), defect.getProcess()},
                currentLocale);

        sendEmails(recipients, subject, body);
    }

    void handleActionNotification(ActionDto action){
        Locale currentLocale = LocaleContextHolder.getLocale();
        Action newAction = entityService.getActionById(action.getId());
        String[] recipients = fetchRecipientsEmailAddresses(newAction.getAssignedUsers());
        String subject = prepareSubject("email.subject.newAction", action.getId(), currentLocale);

        String body = prepareBody("email.body.newAction",
                new Object[]{action.getId(), action.getDescription(), action.getDueDate()},
                currentLocale);

        sendEmails(recipients, subject, body);
    }

    String[] fetchRecipientsEmailAddresses(Set<User> recipients){
        return recipients.stream()
                .map(User::getMail)
                .toArray(String[]::new);
    }

    String prepareSubject(String messageKey, Object arg, Locale locale){
        String subjectTemplate = messageSource.getMessage(messageKey, null, locale);
        return subjectTemplate.formatted(arg);
    }

    String prepareBody(String messageKey, Object[] args, Locale locale){
        String bodyTemplate = messageSource.getMessage(messageKey, null, locale);
        return bodyTemplate.formatted(args);
    }

    void sendEmails(String[] recipients, String subject, String body){
        List<String> failedRecipients = new ArrayList<>();
        for(String recipient : recipients){
            try {
                emailService.sendSimpleEmail(recipient, subject, body);
            } catch (Exception e) {
                failedRecipients.add(recipient);
            }
        }
        if (!failedRecipients.isEmpty()){
            log.error("Failed to send emails to recipients: {}", String.join(", ", failedRecipients));
        }
    }
}
