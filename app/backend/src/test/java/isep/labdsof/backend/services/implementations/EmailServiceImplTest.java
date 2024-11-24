package isep.labdsof.backend.services.implementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() throws Exception {
        Field mailServerUsernameField = EmailServiceImpl.class.getDeclaredField("mailServerUsername");
        mailServerUsernameField.setAccessible(true);
        mailServerUsernameField.set(emailService, "test@example.com");
    }

    @Test
    void testSendMail() {
        // Arrange
        String recipient = "recipient@example.com";
        String title = "Test Email";
        String messageText = "This is a test email.";

        SimpleMailMessage expectedMessage = new SimpleMailMessage();
        expectedMessage.setFrom("test@example.com");
        expectedMessage.setTo(recipient);
        expectedMessage.setSubject(title);
        expectedMessage.setText(messageText);

        // Act
        emailService.sendMail(recipient, title, messageText);

        // Assert
        verify(javaMailSender, times(1)).send(refEq(expectedMessage));
    }
}

