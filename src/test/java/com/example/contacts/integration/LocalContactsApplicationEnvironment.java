package com.example.contacts.integration;

import org.microshed.testing.*;

public class LocalContactsApplicationEnvironment implements ApplicationEnvironment {

    private static final String APPLICATION_URL = "http://localhost:8080";

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public int getPriority() {
        return 1_000;
    }

    @Override
    public void start() {
        // The server is expected to be started externally via liberty:dev.
    }

    @Override
    public String getApplicationURL() {
        return APPLICATION_URL;
    }
}
