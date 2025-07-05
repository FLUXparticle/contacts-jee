package com.example.contacts.dao;

import com.example.contacts.entity.*;
import jakarta.ejb.*;
import jakarta.persistence.*;

import java.util.*;

@Stateless
public class NameDAO {

    @PersistenceContext(unitName = "ContactsPU")
    private EntityManager em;

    public List<Name> findAll() {
        TypedQuery<Name> query = em.createQuery("SELECT n FROM Name n", Name.class);
        return query.getResultList();
    }

    public Name findById(Long id) {
        return em.find(Name.class, id);
    }

    public void save(Name name) {
        em.persist(name);
    }

    public void update(Name name) {
        em.merge(name);
    }

    public void delete(Long id) {
        Name name = findById(id);
        if (name != null) {
            em.remove(name);
        }
    }
}