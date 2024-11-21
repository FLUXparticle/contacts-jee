package com.example.contacts.service;

import com.example.contacts.dao.*;
import com.example.contacts.entity.*;
import jakarta.enterprise.context.*;
import jakarta.inject.*;

import java.util.*;

@CriteriaPrime
@ApplicationScoped
public class CriteriaPrimeContactQueryService implements PrimeContactQueryService {

    @Inject
    @CriteriaPrime
    private PrimeContactDataAccess dataAccess;

    @Override
    public int countContacts(String searchQuery) {
        return dataAccess.countContacts(searchQuery);
    }

    @Override
    public List<Contact> getContactsPaginated(int first, int pageSize, String sortField, String sortDirection, String searchQuery) {
        return dataAccess.findContactsPaginated(first, pageSize, sortField, sortDirection, searchQuery);
    }
}
