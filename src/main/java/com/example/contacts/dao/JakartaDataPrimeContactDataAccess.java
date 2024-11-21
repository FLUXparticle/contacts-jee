package com.example.contacts.dao;

import com.example.contacts.entity.*;
import jakarta.enterprise.context.*;
import jakarta.inject.*;

import java.util.*;

@JakartaDataPrime
@ApplicationScoped
public class JakartaDataPrimeContactDataAccess implements PrimeContactDataAccess {

    @Inject
    private JakartaDataPrimeContactRepository repository;

    @Override
    public int countContacts(String searchQuery) {
        return 0;
    }

    @Override
    public List<Contact> findContactsPaginated(int first, int pageSize, String sortField, String sortDirection, String searchQuery) {
        return List.of();
    }
}
