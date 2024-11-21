package com.example.contacts.api;

import java.util.*;

public class PrimeContactPageResponse {

    private int totalCount;
    private List<String> names = List.of();

    public PrimeContactPageResponse() {
    }

    public PrimeContactPageResponse(int totalCount, List<String> names) {
        this.totalCount = totalCount;
        this.names = List.copyOf(names);
    }

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
        this.names = List.copyOf(names);
    }
}
