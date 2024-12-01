package com.example.contacts.dao;

import com.example.contacts.entity.*;
import jakarta.ejb.*;
import jakarta.persistence.*;

import java.util.*;

@Stateless
public class TransactionDAO {

    @PersistenceContext(unitName = "ContactsPU")
    private EntityManager em;

    public void save(Transaction transaction) {
        em.persist(transaction);
    }

    public List<Transaction> findByContactId(Long contactId) {
        TypedQuery<Transaction> query = em.createQuery(
            "SELECT t FROM Transaction t WHERE t.contact.id = :contactId ORDER BY t.timestamp DESC",
            Transaction.class);
        query.setParameter("contactId", contactId);
        return query.getResultList();
    }

}
