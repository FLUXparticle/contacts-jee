package com.example.contacts.integration;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Path("/prime-contacts/criteria")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface CriteriaPrimeContactsClient extends PrimeContactsClient {
}
