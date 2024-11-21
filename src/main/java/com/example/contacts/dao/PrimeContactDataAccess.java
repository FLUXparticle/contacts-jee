package com.example.contacts.dao;

import com.example.contacts.entity.*;

import java.util.*;

public interface PrimeContactDataAccess {

    int countContacts(String searchQuery);

    List<Contact> findContactsPaginated(int first, int pageSize, String sortField, String sortDirection, String searchQuery);
}
