package com.example.contacts.integration;

import org.microshed.testing.jaxrs.*;
import org.microshed.testing.jupiter.*;

@MicroShedTest
public class JakartaDataPrimeContactSelectionIT extends AbstractPrimeContactSelectionIT {

    @RESTClient
    public static JakartaDataPrimeContactsClient primeContacts;

    @Override
    protected PrimeContactsClient client() {
        return primeContacts;
    }
}
