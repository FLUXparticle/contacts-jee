package com.example.contacts.service;

import com.example.contacts.entity.*;
import jakarta.annotation.*;
import jakarta.ejb.Singleton;
import jakarta.ejb.*;
import jakarta.inject.*;

@Startup
@Singleton
public class DataInitializer {

    @Inject
    private ContactService contactService;

    @PostConstruct
    public void init() {
        // Prüfen, ob bereits Daten vorhanden sind
        if (contactService.getAllContacts().isEmpty()) {
            for (int i = 1; i <= 100; i++) {
                Contact contact = new Contact();
                contact.setName("Name " + i);
                contact.setEmail("email" + i + "@example.com");
                contact.setAddress("Address " + i);
                contactService.saveContact(contact);
            }
        }
    }

}
