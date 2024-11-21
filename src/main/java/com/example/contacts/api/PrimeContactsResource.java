package com.example.contacts.api;

import com.example.contacts.dao.*;
import com.example.contacts.entity.*;
import jakarta.inject.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.*;

@Path("/prime-contacts")
@Produces(MediaType.APPLICATION_JSON)
public class PrimeContactsResource {

    @Inject
    @JpqlPrime
    private PrimeContactDataAccess jpqlDataAccess;

    @Inject
    @CriteriaPrime
    private PrimeContactDataAccess criteriaDataAccess;

    @Inject
    @JakartaDataPrime
    private PrimeContactDataAccess jakartaDataDataAccess;

    @GET
    @Path("/jpql")
    public PrimeContactPageResponse getJpqlPage(
            @QueryParam("searchQuery") String searchQuery,
            @DefaultValue("0") @QueryParam("first") int first,
            @DefaultValue("2") @QueryParam("pageSize") int pageSize,
            @DefaultValue("name") @QueryParam("sortField") String sortField,
            @DefaultValue("ASC") @QueryParam("sortDirection") String sortDirection
    ) {
        return buildResponse(jpqlDataAccess, searchQuery, first, pageSize, sortField, sortDirection);
    }

    @GET
    @Path("/criteria")
    public PrimeContactPageResponse getCriteriaPage(
            @QueryParam("searchQuery") String searchQuery,
            @DefaultValue("0") @QueryParam("first") int first,
            @DefaultValue("2") @QueryParam("pageSize") int pageSize,
            @DefaultValue("name") @QueryParam("sortField") String sortField,
            @DefaultValue("ASC") @QueryParam("sortDirection") String sortDirection
    ) {
        return buildResponse(criteriaDataAccess, searchQuery, first, pageSize, sortField, sortDirection);
    }

    @GET
    @Path("/jakarta-data")
    public PrimeContactPageResponse getJakartaDataPage(
            @QueryParam("searchQuery") String searchQuery,
            @DefaultValue("0") @QueryParam("first") int first,
            @DefaultValue("2") @QueryParam("pageSize") int pageSize,
            @DefaultValue("name") @QueryParam("sortField") String sortField,
            @DefaultValue("ASC") @QueryParam("sortDirection") String sortDirection
    ) {
        return buildResponse(jakartaDataDataAccess, searchQuery, first, pageSize, sortField, sortDirection);
    }

    private static PrimeContactPageResponse buildResponse(
            PrimeContactDataAccess dataAccess,
            String searchQuery,
            int first,
            int pageSize,
            String sortField,
            String sortDirection
    ) {
        List<String> names = dataAccess.findContactsPaginated(first, pageSize, sortField, sortDirection, searchQuery)
                .stream()
                .map(Contact::getName)
                .toList();

        return new PrimeContactPageResponse(dataAccess.countContacts(searchQuery), names);
    }
}
