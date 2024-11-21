package com.example.contacts.service;

import com.example.contacts.entity.*;

import java.util.*;

public interface PrimeContactQueryService {

    int countContacts(String searchQuery);

    List<Contact> getContactsPaginated(int first, int pageSize, String sortField, String sortDirection, String searchQuery);
}
