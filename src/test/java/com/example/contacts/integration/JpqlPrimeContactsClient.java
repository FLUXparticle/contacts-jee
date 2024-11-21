package com.example.contacts.integration;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Path("/prime-contacts/jpql")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface JpqlPrimeContactsClient extends PrimeContactsClient {
}
