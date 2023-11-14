package com.example.defecttrackerserver.notification;

import com.example.defecttrackerserver.TestHelper;
import com.example.defecttrackerserver.core.action.Action;
import com.example.defecttrackerserver.core.action.ActionDto;
import com.example.defecttrackerserver.core.coreService.EntityService;
import com.example.defecttrackerserver.core.defect.defect.DefectDto;
import com.example.defecttrackerserver.core.lot.lot.Lot;
import com.example.defecttrackerserver.core.lot.material.Material;
import com.example.defecttrackerserver.core.user.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationAspectTest {
    @Mock
    EmailService emailService;

    @Mock
    MessageSource messageSource;

    @Mock
    EntityService entityService;

    @InjectMocks
    NotificationAspect notificationAspect;

    @BeforeEach
    void setUp() {
        LocaleContextHolder.setLocale(Locale.US);
    }

    @Test
    void handleDefectNotification() {
        DefectDto defectDto = TestHelper.setUpDefectDto();
        Lot lot = TestHelper.setUpLot();
        User user = TestHelper.setUpUser();
        Material material = TestHelper.setUpMaterial();
        material.setResponsibleUsers(Set.of(user));
        lot.setMaterial(material);

        when(entityService.getLotById(anyInt())).thenReturn(lot);
        when(messageSource.getMessage(anyString(), any(), any(Locale.class))).thenReturn("subject template %s", "body template %s %s %s");

        notificationAspect.handleDefectNotification(defectDto);

        verify(emailService).sendSimpleEmail(anyString(), anyString(), anyString());
    }

    @Test
    void handleActionNotification() {
        User user = TestHelper.setUpUser();
        ActionDto actionDto = TestHelper.setUpActionDto();
        Action action = TestHelper.setUpAction();
        action.setAssignedUsers(Set.of(user));

        when(entityService.getActionById(anyInt())).thenReturn(action);
        when(messageSource.getMessage(anyString(), any(), any(Locale.class))).thenReturn("subject template %s", "body template %s %s %s");

        notificationAspect.handleActionNotification(actionDto);

        verify(emailService).sendSimpleEmail(anyString(), anyString(), anyString());
    }

    @Test
    void fetchRecipientsEmailAddresses() {
        User user1 = new User();
        user1.setId(1);
        user1.setMail("user1@example.com");
        User user2 = new User();
        user2.setId(2);
        user2.setMail("user2@example.com");
        Set<User> recipients = new HashSet<>(Arrays.asList(user1, user2));

        String[] emails = notificationAspect.fetchRecipientsEmailAddresses(recipients);

        assertArrayEquals(new String[]{"user1@example.com", "user2@example.com"}, emails);
    }

    @Test
    void prepareSubject() {
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Subject %s");
        String subject = notificationAspect.prepareSubject("key", "arg", Locale.US);

        assertEquals("Subject arg", subject);
    }

    @Test
    void prepareBody() {
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Body %s %s %s");
        String body = notificationAspect.prepareBody("key", new Object[]{"arg1", "arg2", "arg3"}, Locale.US);

        assertEquals("Body arg1 arg2 arg3", body);
    }

    @Test
    void sendEmails() {
        doNothing().when(emailService).sendSimpleEmail(anyString(), anyString(), anyString());
        String[] recipients = new String[]{"recipient1@example.com", "recipient2@example.com"};

        notificationAspect.sendEmails(recipients, "subject", "body");

        verify(emailService, times(2)).sendSimpleEmail(anyString(), anyString(), anyString());
    }
}