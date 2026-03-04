package com.example.contacts.controller;

import com.example.contacts.entity.*;
import com.example.contacts.service.*;
import jakarta.enterprise.context.*;
import jakarta.faces.application.*;
import jakarta.faces.context.*;
import jakarta.inject.*;
import jakarta.persistence.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;

@Named
@ConversationScoped
public class ContactBean implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactBean.class);

    @Inject
    private ContactService contactService;

    @Inject
    private Conversation conversation;

    private Long contactId;

    private Contact contact = new Contact();

    private Contact databaseContact;

    private boolean optimisticLockConflict;

    private List<Contact> contacts;

    private String newAddress;

    // Methode, um den Kontakt anhand der ID zu laden
    public void loadContact() {
        if (FacesContext.getCurrentInstance().isPostback()) {
            return;
        }

        beginConversationIfNeeded();
        LOGGER.info("loadContact({})", contactId);
        if (contactId != null) {
            contact = copyContact(contactService.getContact(contactId));
            databaseContact = null;
            optimisticLockConflict = false;
            LOGGER.info("version = {}", contact.getVersion());
        }
    }

    // Aktionen
    public String addContact() {
        contactService.saveContact(contact);
        addInfoMessage("Contact added successfully.");
        contact = new Contact(); // Formular zurücksetzen
        contacts = null; // Liste aktualisieren
        endConversationIfNeeded();
        return "contacts?faces-redirect=true"; // Navigation zur Kontaktliste
    }

    public String updateContact() {
        LOGGER.info("updateContact()");

        try {
            contactService.updateContact(contact);

            addInfoMessage("Contact updated successfully.");
            contacts = null; // Liste aktualisieren
            optimisticLockConflict = false;
            databaseContact = null;
            endConversationIfNeeded();

            return "contacts?faces-redirect=true";
        } catch (Exception e) {
            Throwable cause = e;

            while (cause != null) {
                LOGGER.info("ContactBean.updateContact: cause = {}", cause.toString());
                if (cause instanceof OptimisticLockException) {
                    handleOptimisticLockConflict();
                    return "mergeContact";
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

    public void handleOptimisticLockConflict() {
        beginConversationIfNeeded();

        addErrorMessage("Datensatz wurde zwischenzeitlich geändert. Bitte Konflikt auflösen.");

        Contact current = contactService.getContact(contact.getId());
        if (current == null) {
            addErrorMessage("Kontakt existiert nicht mehr.");
            optimisticLockConflict = false;
            databaseContact = null;
            return;
        }

        databaseContact = copyContact(current);
        contact.setVersion(databaseContact.getVersion());
        optimisticLockConflict = true;
    }

    public String retryUpdateContact() {
        return updateContact();
    }

    public String cancelEdit() {
        resetEditState();
        endConversationIfNeeded();
        return "contacts?faces-redirect=true";
    }

    public String cancelMerge() {
        return cancelEdit();
    }

    public void ensureMergeContext() throws IOException {
        if (!optimisticLockConflict || databaseContact == null) {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect(externalContext.getRequestContextPath() + "/contacts.xhtml");
        }
    }

    public void adoptDatabaseName() {
        if (databaseContact != null) {
            contact.setName(databaseContact.getName());
        }
    }

    public void adoptDatabaseEmail() {
        if (databaseContact != null) {
            contact.setEmail(databaseContact.getEmail());
        }
    }

    public void adoptDatabaseAddress() {
        if (databaseContact != null) {
            contact.setAddress(databaseContact.getAddress());
        }
    }

    public void adoptAllDatabaseFields() {
        adoptDatabaseName();
        adoptDatabaseEmail();
        adoptDatabaseAddress();
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

    public Contact getDatabaseContact() {
        return databaseContact;
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


    private void resetEditState() {
        contactId = null;
        contact = new Contact();
        databaseContact = null;
        optimisticLockConflict = false;
    }

    private void beginConversationIfNeeded() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
    }

    private void endConversationIfNeeded() {
        if (!conversation.isTransient()) {
            conversation.end();
        }

    }

    private static Contact copyContact(Contact source) {
        if (source == null) {
            return null;
        }
        Contact copy = new Contact();
        copy.setId(source.getId());
        copy.setName(source.getName());
        copy.setEmail(source.getEmail());
        copy.setAddress(source.getAddress());
        copy.setVersion(source.getVersion());
        return copy;
    }
}
