package com.example.contacts.service;

import com.example.contacts.dao.*;
import com.example.contacts.entity.*;
import jakarta.ejb.*;
import jakarta.inject.*;
import jakarta.persistence.*;
import org.slf4j.*;

import java.util.*;

@Stateless
public class ContactService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactService.class);

    @Inject
    private ContactDAO contactDAO;

    public List<Contact> getAllContacts() {
        return contactDAO.findAll();
    }

    public Contact getContact(Long id) {
        return contactDAO.findById(id);
    }

    public void saveContact(Contact contact) {
        contactDAO.save(contact);
    }

    public void updateContact(Contact contact) throws OptimisticLockException {
        LOGGER.info("updateContact({})", contact);
        contactDAO.update(contact);
    }

    public void deleteContact(Long id) {
        contactDAO.delete(id);
    }

}
