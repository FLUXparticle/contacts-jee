package com.example.contacts.api;

import com.example.weld.*;
import jakarta.inject.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.*;

@Path("/scope-demo")
@Produces(MediaType.TEXT_PLAIN)
public class ScopeDemoResource {

    @Inject
    private ScopeDemoRequestBean requestBean;

    @Inject
    private ScopeDemoService scopeDemoService;

    @GET
    public String getScopeDemo(@Context UriInfo uriInfo) {
        requestBean.setRequestUri(uriInfo.getRequestUri().toASCIIString());

        String resourceRequestId = requestBean.getRequestId();
        String serviceRequestId = scopeDemoService.getCurrentRequestId();

        return """
                resourceRequestClass=%s,
                serviceRequestClass=%s,
                applicationServiceClass=%s,
                resourceRequestId=%s
                serviceRequestId=%s
                applicationServiceId=%s
                requestUri=%s
                requestCreatedAt=%s
                sameRequest=%s
                """.formatted(
                Objects.toIdentityString(requestBean),
                Objects.toIdentityString(scopeDemoService.getCurrentRequestClass()),
                Objects.toIdentityString(scopeDemoService),
                resourceRequestId,
                serviceRequestId,
                scopeDemoService.getServiceId(),
                scopeDemoService.getCurrentRequestUri(),
                requestBean.getCreatedAt(),
                resourceRequestId.equals(serviceRequestId)
        );
    }

}
