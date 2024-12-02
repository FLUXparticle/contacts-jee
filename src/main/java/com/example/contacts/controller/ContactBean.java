package com.example.contacts.controller;

import com.example.contacts.entity.*;
import com.example.contacts.service.*;
import jakarta.annotation.*;
import jakarta.faces.application.*;
import jakarta.faces.context.*;
import jakarta.faces.view.*;
import jakarta.inject.*;
import org.primefaces.model.*;
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

    private LazyDataModel<Contact> lazyModel;

    private String searchQuery;

    @PostConstruct
    public void init() {
        lazyModel = new LazyDataModel<>() {
            @Override
            public int count(Map<String, FilterMeta> map) {
                System.out.println("count: map = " + map);
                return contactService.countContacts(searchQuery);
            }

            @Override
            public List<Contact> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                System.out.println("load: first = " + first + ", pageSize = " + pageSize + ", sortBy = " + sortBy + ", filterBy = " + filterBy);
                try {
                    // Künstliche Verzögerung
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOGGER.error(e.getMessage());
                }

                // Sortierparameter extrahieren
                String sortField = null;
                String sortDirection = "ASC";

                if (sortBy != null && !sortBy.isEmpty()) {
                    Map.Entry<String, SortMeta> entry = sortBy.entrySet().iterator().next();
                    sortField = entry.getKey();
                    SortOrder sortOrder = entry.getValue().getOrder();
                    sortDirection = (sortOrder == SortOrder.ASCENDING) ? "ASC" : "DESC";
                }

                // Daten laden
                return contactService.getContactsPaginated(first, pageSize, sortField, sortDirection, searchQuery);
            }
        };
    }

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
        return "contacts?faces-redirect=true"; // Navigation zur Kontaktliste
    }

    public String updateContact() {
        LOGGER.info("updateContact()");

        try {
            contactService.updateContact(contact);

            addInfoMessage("Contact updated successfully.");

            return "contacts?faces-redirect=true";
        } catch (Exception e) {
            Throwable cause = e;

            while (cause != null) {
                LOGGER.info("ContactBean.updateContact: cause = {}", cause.toString());
                if (cause instanceof jakarta.persistence.OptimisticLockException) {
                    addErrorMessage(cause.getMessage());
                    return null; // Auf der gleichen Seite bleiben
                }
                cause = cause.getCause();
            }

            throw e;
        }
    }

    public void deleteContact(Long id) {
        contactService.deleteContact(id);
        addInfoMessage("Contact deleted successfully.");
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

    public LazyDataModel<Contact> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<Contact> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        addInfoMessage("searchQuery = " + searchQuery);
        this.searchQuery = searchQuery;
    }

    private static void addInfoMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
    }

    private static void addErrorMessage(String error) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, error, null));
    }
}