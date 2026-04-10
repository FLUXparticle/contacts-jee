package com.example.contacts.dao;

import com.example.contacts.entity.*;
import jakarta.enterprise.context.*;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;

import java.util.*;

@CriteriaPrime
@ApplicationScoped
public class CriteriaPrimeContactDataAccess implements PrimeContactDataAccess {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("id", "name", "email", "address");

    @PersistenceContext(unitName = "ContactsPU")
    private EntityManager em;

    @Override
    public int countContacts(String searchQuery) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Contact> contact = query.from(Contact.class);

        query.select(cb.count(contact));

        Predicate filter = buildSearchPredicate(searchQuery, cb, contact);
        if (filter != null) {
            query.where(filter);
        }

        return em.createQuery(query).getSingleResult().intValue();
    }

    @Override
    public List<Contact> findContactsPaginated(int first, int pageSize, String sortField, String sortDirection, String searchQuery) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Contact> query = cb.createQuery(Contact.class);
        Root<Contact> contact = query.from(Contact.class);

        query.select(contact);

        Predicate filter = buildSearchPredicate(searchQuery, cb, contact);
        if (filter != null) {
            query.where(filter);
        }

        String resolvedSortField = resolveSortField(sortField);
        if (resolvedSortField != null) {
            Path<?> sortPath = contact.get(resolvedSortField);
            query.orderBy("DESC".equalsIgnoreCase(sortDirection) ? cb.desc(sortPath) : cb.asc(sortPath));
        }

        TypedQuery<Contact> typedQuery = em.createQuery(query);
        typedQuery.setFirstResult(first);
        typedQuery.setMaxResults(pageSize);
        return typedQuery.getResultList();
    }

    private static Predicate buildSearchPredicate(String searchQuery, CriteriaBuilder cb, Root<Contact> contact) {
        if (!hasSearchQuery(searchQuery)) {
            return null;
        }

        String pattern = toSearchPattern(searchQuery);
        return cb.or(
                cb.like(cb.lower(contact.get("name")), pattern),
                cb.like(cb.lower(contact.get("email")), pattern),
                cb.like(cb.lower(contact.get("address")), pattern)
        );
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
}
