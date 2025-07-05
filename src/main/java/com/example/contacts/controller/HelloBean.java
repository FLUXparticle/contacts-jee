package com.example.contacts.controller;

import jakarta.enterprise.context.*;
import jakarta.inject.*;

@Named
@RequestScoped
public class HelloBean {

    private String name = "World";

    // getter/setter

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
