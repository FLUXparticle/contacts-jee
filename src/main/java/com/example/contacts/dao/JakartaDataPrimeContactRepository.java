package com.example.contacts.dao;

import com.example.contacts.entity.*;
import jakarta.data.repository.*;

@Repository
public interface JakartaDataPrimeContactRepository extends BasicRepository<Contact, Long> {

}
