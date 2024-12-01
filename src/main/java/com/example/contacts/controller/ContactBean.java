package com.example.contacts.controller;

import com.example.contacts.entity.*;
import com.example.contacts.service.*;
import jakarta.faces.application.*;
import jakarta.faces.context.*;
import jakarta.faces.view.*;
import jakarta.inject.*;
import jakarta.persistence.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;

@Named
@ViewScoped
public class ContactBean implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactBean.class);

    @Inject
    private ContactService contactService;

    private Long contactId;

    private Contact contact = new Contact();

    private List<Contact> contacts;

    private String newAddress;

    // Methode, um den Kontakt anhand der ID zu laden
    public void loadContact() {
        LOGGER.info("loadContact({})", contactId);
        if (contactId != null) {
            contact = contactService.getContact(contactId);
            LOGGER.info("version = {}", contact.getVersion());
        }
    }

    // Aktionen
    public String addContact() {
        contactService.saveContact(contact);
        addInfoMessage("Contact added successfully.");
        contact = new Contact(); // Formular zurücksetzen
        contacts = null; // Liste aktualisieren
        return "contacts?faces-redirect=true"; // Navigation zur Kontaktliste
    }

    public String updateContact() {
        LOGGER.info("updateContact()");

        try {
            contactService.updateContact(contact);

            addInfoMessage("Contact updated successfully.");
            contacts = null; // Liste aktualisieren

            return "contacts?faces-redirect=true";
        } catch (Exception e) {
            Throwable cause = e;

            while (cause != null) {
                LOGGER.info("ContactBean.updateContact: cause = {}", cause.toString());
                if (cause instanceof OptimisticLockException) {
                    addErrorMessage(cause.getMessage());
                    return null; // Auf der gleichen Seite bleiben
                }
                cause = cause.getCause();
            }

            throw e;
        }
    }

    public String deleteContact(Long id) {
        contactService.deleteContact(id);
        addInfoMessage("Contact deleted successfully.");
        contacts = null; // Liste aktualisieren
        return "contacts?faces-redirect=true";
    }

    public String bulkUpdateContacts() {
        try {
            List<Contact> contacts = getContacts();
            contactService.updateMultipleContacts(contacts, newAddress);
            addInfoMessage("Contacts updated successfully.");
            return "contacts?faces-redirect=true";
        } catch (Exception e) {
            Throwable cause = e;

            while (cause != null) {
                LOGGER.info("ContactBean.bulkUpdateContacts: cause = {}", cause.toString());
                if (cause instanceof OptimisticLockException) {
                    addErrorMessage(cause.getMessage());
                    return null; // Auf der gleichen Seite bleiben
                }
                cause = cause.getCause();
            }

            throw e;
        }
    }

    // Getter und Setter
    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public List<Contact> getContacts() {
        if (contacts == null) {
            contacts = contactService.getAllContacts();
        }
        return contacts;
    }

    public String getNewAddress() {
        return newAddress;
    }

    public void setNewAddress(String newAddress) {
        this.newAddress = newAddress;
    }

    private static void addInfoMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
    }

    private static void addErrorMessage(String error) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, error, null));
    }

}
