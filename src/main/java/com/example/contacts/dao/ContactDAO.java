package com.example.contacts.dao;

import com.example.contacts.entity.*;
import jakarta.enterprise.context.*;
import jakarta.persistence.*;
import jakarta.transaction.*;

import java.util.*;

@ApplicationScoped
public class ContactDAO {

    @PersistenceContext(unitName = "ContactsPU")
    private EntityManager em;

    public List<Contact> findAll() {
        TypedQuery<Contact> query = em.createQuery("SELECT c FROM Contact c", Contact.class);
        return query.getResultList();
    }

    public Contact findById(Long id) {
        return em.find(Contact.class, id);
    }

    @Transactional
    public void save(Contact contact) {
        em.persist(contact);
    }

    @Transactional
    public void update(Contact contact) {
        em.merge(contact);
    }

    public void delete(Long id) {
        Contact contact = findById(id);
        if (contact != null) {
            em.remove(contact);
        }
    }

    public List<Contact> search(String queryStr) {
        TypedQuery<Contact> query = em.createQuery(
            "SELECT c FROM Contact c WHERE c.name LIKE :query OR c.email LIKE :query OR c.address LIKE :query",
            Contact.class);
        query.setParameter("query", "%" + queryStr + "%");
        return query.getResultList();
    }

    public int countContacts(String searchQuery) {
        String queryStr = "SELECT COUNT(c) FROM Contact c";
        if (searchQuery != null && !searchQuery.isEmpty()) {
            queryStr += " WHERE c.name LIKE :query OR c.email LIKE :query OR c.address LIKE :query";
        }
        TypedQuery<Long> query = em.createQuery(queryStr, Long.class);
        if (searchQuery != null && !searchQuery.isEmpty()) {
            query.setParameter("query", "%" + searchQuery + "%");
        }
        return query.getSingleResult().intValue();
    }

    public List<Contact> findContactsPaginated(int first, int pageSize, String sortField, String sortDirection, String searchQuery) {
        String queryStr = "SELECT c FROM Contact c";
        if (searchQuery != null && !searchQuery.isEmpty()) {
            queryStr += " WHERE c.name LIKE :query OR c.email LIKE :query OR c.address LIKE :query";
        }
        if (sortField != null) {
            queryStr += " ORDER BY c." + sortField + " " + sortDirection;
        }
        TypedQuery<Contact> query = em.createQuery(queryStr, Contact.class);
        if (searchQuery != null && !searchQuery.isEmpty()) {
            query.setParameter("query", "%" + searchQuery + "%");
        }
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

}
