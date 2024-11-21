package com.example.contacts.controller;

import jakarta.faces.view.*;
import jakarta.inject.*;

import java.io.*;

@Named
@ViewScoped
public class RepeatBean implements Serializable {

    private String input;
    private String result;

    public void submit() {
        result = input;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getResult() {
        return result;
    }
}
