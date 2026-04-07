package com.example.contacts.dao;

import com.example.contacts.entity.*;
import jakarta.data.*;
import jakarta.data.page.*;
import jakarta.data.repository.*;

@Repository
public interface JakartaDataPrimeContactRepository extends BasicRepository<Contact, Long> {

    long count();

    long countByNameIgnoreCaseContainsOrEmailIgnoreCaseContainsOrAddressIgnoreCaseContains(
            String namePart,
            String emailPart,
            String addressPart
    );

    Page<Contact> findByNameIgnoreCaseContainsOrEmailIgnoreCaseContainsOrAddressIgnoreCaseContains(
            String namePart,
            String emailPart,
            String addressPart,
            PageRequest pageRequest,
            Order<Contact> order
    );
}
