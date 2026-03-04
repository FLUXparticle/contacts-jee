package com.example.contacts.service;

import com.example.contacts.dao.*;
import com.example.contacts.entity.*;
import jakarta.annotation.*;
import jakarta.ejb.*;
import jakarta.ejb.Singleton;
import jakarta.inject.*;

@Singleton
@Startup
public class ContactDataInitializer {

    @Inject
    private ContactDAO contactDAO;

    @PostConstruct
    public void seedContacts() {
        for (int i = 1; i <= 25; i++) {
            Contact contact = new Contact();
            contact.setName("Kontakt " + i);
            contact.setEmail("kontakt" + i + "@example.com");
            contact.setAddress("Musterstrasse " + i);
            contactDAO.save(contact);
        }
    }
}
