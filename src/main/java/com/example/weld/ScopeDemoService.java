package com.example.weld;

import jakarta.enterprise.context.*;
import jakarta.inject.*;

import java.util.*;

@ApplicationScoped
public class ScopeDemoService {

    private final String serviceId = UUID.randomUUID().toString();

    @Inject
    private ScopeDemoRequestBean requestBean;

    public String getServiceId() {
        return serviceId;
    }

    public ScopeDemoRequestBean getCurrentRequestClass() {
        return requestBean;
    }

    public String getCurrentRequestId() {
        return requestBean.getRequestId();
    }

    public String getCurrentRequestUri() {
        return requestBean.getRequestUri();
    }
}
