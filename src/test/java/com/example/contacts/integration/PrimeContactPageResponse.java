package com.example.contacts.integration;

import java.util.*;

public class PrimeContactPageResponse {

    private int totalCount;

    private List<String> names;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }
}
