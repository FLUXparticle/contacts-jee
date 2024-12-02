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

}
