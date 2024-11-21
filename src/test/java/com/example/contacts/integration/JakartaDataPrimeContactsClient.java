package com.example.contacts.integration;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Path("/prime-contacts/jakarta-data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface JakartaDataPrimeContactsClient extends PrimeContactsClient {
}
