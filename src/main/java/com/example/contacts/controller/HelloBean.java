package com.example.contacts.controller;

import jakarta.faces.view.*;
import jakarta.inject.*;

import java.io.*;
import java.util.*;

@Named
@ViewScoped
public class HelloBean implements Serializable {

    private String name = "World";

    private List<String> names = new ArrayList<>();

    // actions
    public void submit() {
        if (name != null && !name.isEmpty()) {
            names.add(name);
        }
    }

    // getter/setter
    public List<String> getNames() {
        return names;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
