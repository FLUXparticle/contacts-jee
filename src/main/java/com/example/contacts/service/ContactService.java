package com.example.contacts.service;

import com.example.contacts.dao.*;
import com.example.contacts.entity.*;
import jakarta.enterprise.context.*;
import jakarta.inject.*;
import jakarta.persistence.*;
import org.slf4j.*;

import java.util.*;

@ApplicationScoped
public class ContactService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactService.class);

    @Inject
    private EmailService emailService;

    @Inject
    private ContactDAO contactDAO;

    @Inject
    @JpqlPrime
    private PrimeContactQueryService primeContactQueryService;

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
        emailService.sendEmail(contact.getEmail());
        contactDAO.update(contact);
    }

    public void deleteContact(Long id) {
        contactDAO.delete(id);
    }

    public int countContacts(String searchQuery) {
        return primeContactQueryService.countContacts(searchQuery);
    }

    public List<Contact> getContactsPaginated(int first, int pageSize, String sortField, String sortDirection, String searchQuery) {
        return primeContactQueryService.getContactsPaginated(first, pageSize, sortField, sortDirection, searchQuery);
    }

    public void updateMultipleContacts(List<Contact> contacts, String newAddress) {
        for (Contact contact : contacts) {
            contact.setAddress(newAddress); // Aktualisierung vornehmen
            updateContact(contact);
        }
    }

}
