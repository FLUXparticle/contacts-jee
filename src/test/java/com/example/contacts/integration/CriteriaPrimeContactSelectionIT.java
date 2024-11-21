package com.example.contacts.integration;

import org.microshed.testing.jaxrs.*;
import org.microshed.testing.jupiter.*;

@MicroShedTest
public class CriteriaPrimeContactSelectionIT extends AbstractPrimeContactSelectionIT {

    @RESTClient
    public static CriteriaPrimeContactsClient primeContacts;

    @Override
    protected PrimeContactsClient client() {
        return primeContacts;
    }
}
