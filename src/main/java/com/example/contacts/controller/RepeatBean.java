package com.example.contacts.controller;

import jakarta.enterprise.context.*;
import jakarta.inject.*;

import java.io.*;

@Named
@RequestScoped
public class RepeatBean implements Serializable {

    private String input = "";
    private String output = "";

    // actions
    public void submit() {
        output = input;
    }

    // getter/setter

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

}
