package com.example.contacts.integration;

import org.microshed.testing.jaxrs.*;
import org.microshed.testing.jupiter.*;

@MicroShedTest
public class JpqlPrimeContactSelectionIT extends AbstractPrimeContactSelectionIT {

    @RESTClient
    public static JpqlPrimeContactsClient primeContacts;

    @Override
    protected PrimeContactsClient client() {
        return primeContacts;
    }
}
