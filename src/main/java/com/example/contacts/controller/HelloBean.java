package com.example.contacts.controller;

import jakarta.enterprise.context.*;
import jakarta.inject.*;

import java.io.*;
import java.util.*;

@Named
@SessionScoped
public class HelloBean implements Serializable {

   private String name = "World";
    private List<String> names = new ArrayList<>();

   // getter/setter

   public String getName() {
       return name;
   }

   public void setName(String name) {
       this.name = name;
   }


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

}
