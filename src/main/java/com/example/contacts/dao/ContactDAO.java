package com.example.contacts.dao;

import com.example.contacts.entity.*;
import jakarta.enterprise.context.*;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
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
        // Erstelle CriteriaBuilder und CriteriaQuery
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        // Root-Element für die Abfrage
        Root<Contact> contact = cq.from(Contact.class);

        // Wähle COUNT
        cq.select(cb.count(contact));

        // Optional: Filter hinzufügen
        if (searchQuery != null && !searchQuery.isEmpty()) {
            String searchPattern = "%" + searchQuery.toLowerCase() + "%";
            Predicate namePredicate = cb.like(cb.lower(contact.get("name")), searchPattern);
            Predicate emailPredicate = cb.like(cb.lower(contact.get("email")), searchPattern);
            Predicate addressPredicate = cb.like(cb.lower(contact.get("address")), searchPattern);
            cq.where(cb.or(namePredicate, emailPredicate, addressPredicate));
        }

        // Abfrage ausführen
        return em.createQuery(cq).getSingleResult().intValue();
    }

    public List<Contact> findContactsPaginated(int first, int pageSize, String sortField, String sortDirection, String searchQuery) {
        // Erstelle CriteriaBuilder und CriteriaQuery
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Contact> cq = cb.createQuery(Contact.class);

        // Root-Element für die Abfrage
        Root<Contact> contact = cq.from(Contact.class);

        // Wähle das Root-Element
        cq.select(contact);

        // Filter hinzufügen
        if (searchQuery != null && !searchQuery.isEmpty()) {
            String searchPattern = "%" + searchQuery.toLowerCase() + "%";
            Predicate namePredicate = cb.like(cb.lower(contact.get("name")), searchPattern);
            Predicate emailPredicate = cb.like(cb.lower(contact.get("email")), searchPattern);
            Predicate addressPredicate = cb.like(cb.lower(contact.get("address")), searchPattern);
            cq.where(cb.or(namePredicate, emailPredicate, addressPredicate));
        }

        // Sortierung hinzufügen
        if (sortField != null && !sortField.isEmpty()) {
            Path<?> sortPath = contact.get(sortField);
            if ("DESC".equalsIgnoreCase(sortDirection)) {
                cq.orderBy(cb.desc(sortPath));
            } else {
                cq.orderBy(cb.asc(sortPath));
            }
        }

        // Erstelle die Abfrage
        TypedQuery<Contact> query = em.createQuery(cq);

        // Paging anwenden
        query.setFirstResult(first);
        query.setMaxResults(pageSize);

        // Ergebnisse zurückgeben
        return query.getResultList();
    }

}
