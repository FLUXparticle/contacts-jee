package com.example.contacts.dao;

import com.example.contacts.entity.*;
import jakarta.enterprise.context.*;
import jakarta.persistence.*;

import java.util.*;

@CriteriaPrime
@ApplicationScoped
public class CriteriaPrimeContactDataAccess implements PrimeContactDataAccess {

    @PersistenceContext(unitName = "ContactsPU")
    private EntityManager em;

    @Override
    public int countContacts(String searchQuery) {
        return 0;
    }

    @Override
    public List<Contact> findContactsPaginated(int first, int pageSize, String sortField, String sortDirection, String searchQuery) {
        return List.of();
    }
}
