package com.example.contacts.controller;

import com.example.contacts.dao.*;
import com.example.contacts.entity.*;
import jakarta.enterprise.context.*;
import jakarta.inject.*;

import java.io.*;
import java.util.*;

@Named
@RequestScoped
public class HelloBean implements Serializable {

    @Inject
    private NameDAO nameDAO;

    private String name = "World";

    // actions
    public void submit() {
        if (name != null && !name.isEmpty()) {
            Name nameEntity = new Name(name);
            nameDAO.save(nameEntity);
        }
    }

    // getter/setter
    public List<Name> getNames() {
        return nameDAO.findAll();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
