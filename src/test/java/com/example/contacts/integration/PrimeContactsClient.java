package com.example.contacts.integration;

import jakarta.ws.rs.*;

public interface PrimeContactsClient {

    @GET
    PrimeContactPageResponse getPage(
            @QueryParam("searchQuery") String searchQuery,
            @QueryParam("first") int first,
            @QueryParam("pageSize") int pageSize,
            @QueryParam("sortField") String sortField,
            @QueryParam("sortDirection") String sortDirection
    );
}
