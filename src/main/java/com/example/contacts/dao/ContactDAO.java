package com.example.contacts.dao;

import com.example.contacts.entity.*;
import jakarta.ejb.*;
import jakarta.persistence.*;

import java.util.*;

@Stateless
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

    public void save(Contact contact) {
        em.persist(contact);
    }

    public void update(Contact contact) {
        em.merge(contact);
    }

    public void delete(Long id) {
        Contact contact = findById(id);
        if (contact != null) {
            em.remove(contact);
        }
    }

}
