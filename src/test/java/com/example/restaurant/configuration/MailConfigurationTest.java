package com.example.restaurant.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@SpringBootTest
@Import(MailConfiguration.class)
public class MailConfigurationTest {

    @Autowired
    private JavaMailSender mailSender;

    @Test
    public void testMailSenderConfig() {
        assertNotNull(mailSender, "MailSender should not be null");

        // Cast to JavaMailSenderImpl to access properties for validation
        JavaMailSenderImpl mailSenderImpl = (JavaMailSenderImpl) mailSender;

        assertEquals("smtp.ethereal.email", mailSenderImpl.getHost());
        assertEquals(587, mailSenderImpl.getPort());
        assertEquals("carolina.kunde24@ethereal.email", mailSenderImpl.getUsername());
        assertEquals("SMv7suqbP65YZBGTgJ", mailSenderImpl.getPassword());

        assertEquals("true", mailSenderImpl.getJavaMailProperties().getProperty("mail.debug"));
        assertEquals("true", mailSenderImpl.getJavaMailProperties().getProperty("mail.smtp.auth"));
        assertEquals("true", mailSenderImpl.getJavaMailProperties().getProperty("mail.smtp.starttls.enable"));

    }

}
