package com.example.contacts.integration;

import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractPrimeContactSelectionIT {

    protected abstract PrimeContactsClient client();

    @Test
    void returnsExpectedFilteredPage() {
        PrimeContactPageResponse response = client().getPage("2", 0, 3, "id", "ASC");

        assertEquals(8, response.getTotalCount());
        assertEquals(List.of("Kontakt 2", "Kontakt 12", "Kontakt 20"), response.getNames());
    }

    @Test
    void returnsExpectedUnfilteredDescendingPage() {
        PrimeContactPageResponse response = client().getPage(null, 0, 2, "id", "DESC");

        assertEquals(25, response.getTotalCount());
        assertEquals(List.of("Kontakt 25", "Kontakt 24"), response.getNames());
    }
}
