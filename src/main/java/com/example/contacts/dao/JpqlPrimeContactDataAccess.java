package com.example.contacts.dao;

import com.example.contacts.entity.*;
import jakarta.enterprise.context.*;
import jakarta.persistence.*;

import java.util.*;

@JpqlPrime
@ApplicationScoped
public class JpqlPrimeContactDataAccess implements PrimeContactDataAccess {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("id", "name", "email", "address");

    @PersistenceContext(unitName = "ContactsPU")
    private EntityManager em;

    @Override
    public int countContacts(String searchQuery) {
        String jpql = "SELECT COUNT(c) FROM Contact c";
        if (hasSearchQuery(searchQuery)) {
            jpql += " WHERE LOWER(c.name) LIKE :query OR LOWER(c.email) LIKE :query OR LOWER(c.address) LIKE :query";
        }

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        if (hasSearchQuery(searchQuery)) {
            query.setParameter("query", toSearchPattern(searchQuery));
        }

        return query.getSingleResult().intValue();
    }

    @Override
    public List<Contact> findContactsPaginated(int first, int pageSize, String sortField, String sortDirection, String searchQuery) {
        String jpql = "SELECT c FROM Contact c";
        if (hasSearchQuery(searchQuery)) {
            jpql += " WHERE LOWER(c.name) LIKE :query OR LOWER(c.email) LIKE :query OR LOWER(c.address) LIKE :query";
        }

        String resolvedSortField = resolveSortField(sortField);
        if (resolvedSortField != null) {
            jpql += " ORDER BY c." + resolvedSortField + " " + resolveSortDirection(sortDirection);
        }

        TypedQuery<Contact> query = em.createQuery(jpql, Contact.class);
        if (hasSearchQuery(searchQuery)) {
            query.setParameter("query", toSearchPattern(searchQuery));
        }

        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    private static boolean hasSearchQuery(String searchQuery) {
        return searchQuery != null && !searchQuery.isBlank();
    }

    private static String toSearchPattern(String searchQuery) {
        return "%" + searchQuery.trim().toLowerCase(Locale.ROOT) + "%";
    }

    private static String resolveSortField(String sortField) {
        if (sortField == null || sortField.isBlank()) {
            return null;
        }

        return ALLOWED_SORT_FIELDS.contains(sortField) ? sortField : "id";
    }

    private static String resolveSortDirection(String sortDirection) {
        return "DESC".equalsIgnoreCase(sortDirection) ? "DESC" : "ASC";
    }
}
