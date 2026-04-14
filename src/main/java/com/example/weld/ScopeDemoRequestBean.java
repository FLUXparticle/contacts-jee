package com.example.weld;

import jakarta.enterprise.context.*;

import java.time.*;
import java.util.*;

@RequestScoped
public class ScopeDemoRequestBean {

    private final String requestId = UUID.randomUUID().toString();
    private final Instant createdAt = Instant.now();
    private String requestUri = "<unknown>";

    public String getRequestId() {
        return requestId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }
}
