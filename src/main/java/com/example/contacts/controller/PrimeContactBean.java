package com.example.contacts.controller;

import com.example.contacts.entity.*;
import com.example.contacts.service.*;
import jakarta.annotation.*;
import jakarta.faces.view.*;
import jakarta.inject.*;
import org.primefaces.model.*;

import java.io.*;
import java.util.*;

@Named
@ViewScoped
public class PrimeContactBean implements Serializable {

    private final ContactService contactService;

    private LazyDataModel<Contact> lazyModel;

    private String searchQuery;

    @Inject
    public PrimeContactBean(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostConstruct
    public void init() {
        lazyModel = new LazyDataModel<>() {
            @Override
            public int count(Map<String, FilterMeta> map) {
                return contactService.countContacts(searchQuery);
            }

            @Override
            public List<Contact> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                String sortField = null;
                String sortDirection = "ASC";

                if (sortBy != null && !sortBy.isEmpty()) {
                    Map.Entry<String, SortMeta> entry = sortBy.entrySet().iterator().next();
                    sortField = entry.getKey();
                    SortOrder sortOrder = entry.getValue().getOrder();
                    sortDirection = sortOrder == SortOrder.ASCENDING ? "ASC" : "DESC";
                }

                return contactService.getContactsPaginated(first, pageSize, sortField, sortDirection, searchQuery);
            }
        };
    }

    public LazyDataModel<Contact> getLazyModel() {
        return lazyModel;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }
}
