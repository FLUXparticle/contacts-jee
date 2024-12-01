package com.example.contacts.service;

import jakarta.ejb.*;
import org.slf4j.*;

@Stateless
public class EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Asynchronous
    // Wird nicht asynchron aufgerufen, wenn die Methode aus der gleichen Klasse aufgerufen wird
    public void sendEmail(String recipient) {
        // Simulierte Verzögerung
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            LOGGER.error(e.toString());
        }
        LOGGER.info("Email sent to: {}", recipient);
    }

}
