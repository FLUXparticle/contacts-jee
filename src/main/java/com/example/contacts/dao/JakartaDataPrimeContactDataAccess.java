package com.example.contacts.dao;

import com.example.contacts.entity.*;
import jakarta.data.*;
import jakarta.data.page.*;
import jakarta.enterprise.context.*;
import jakarta.inject.*;

import java.util.*;

@JakartaDataPrime
@ApplicationScoped
public class JakartaDataPrimeContactDataAccess implements PrimeContactDataAccess {

    @Inject
    private JakartaDataPrimeContactRepository repository;

    @Override
    public int countContacts(String searchQuery) {
        if (hasSearchQuery(searchQuery)) {
            return Math.toIntExact(repository.countByNameIgnoreCaseContainsOrEmailIgnoreCaseContainsOrAddressIgnoreCaseContains(
                    searchQuery,
                    searchQuery,
                    searchQuery
            ));
        }

        return Math.toIntExact(repository.count());
    }

    @Override
    public List<Contact> findContactsPaginated(int first, int pageSize, String sortField, String sortDirection, String searchQuery) {
        PageRequest pageRequest = PageRequest.ofPage(toPageNumber(first, pageSize), pageSize, false);
        Order<Contact> order = Order.by(toSort(sortField, sortDirection));

        if (hasSearchQuery(searchQuery)) {
            return repository.findByNameIgnoreCaseContainsOrEmailIgnoreCaseContainsOrAddressIgnoreCaseContains(
                    searchQuery,
                    searchQuery,
                    searchQuery,
                    pageRequest,
                    order
            ).content();
        }

        return repository.findAll(pageRequest, order).content();
    }

    private static boolean hasSearchQuery(String searchQuery) {
        return searchQuery != null && !searchQuery.isEmpty();
    }

    private static long toPageNumber(int first, int pageSize) {
        return pageSize <= 0 ? 1 : (long) (first / pageSize) + 1;
    }

    private static Sort<Contact> toSort(String sortField, String sortDirection) {
        String field = (sortField == null || sortField.isEmpty()) ? "id" : sortField;
        if ("DESC".equalsIgnoreCase(sortDirection)) {
            return Sort.desc(field);
        }
        return Sort.asc(field);
    }
}
