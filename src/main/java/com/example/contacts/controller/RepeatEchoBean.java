package com.example.contacts.controller;

import jakarta.faces.view.*;
import jakarta.inject.*;

import java.io.*;

@Named
@ViewScoped
public class RepeatEchoBean implements Serializable {

    private String upperText;
    private String lowerText;

    public void submitUpper() {
        lowerText = upperText;
    }

    public void submitLower() {
        upperText = lowerText;
    }

    public String getUpperText() {
        return upperText;
    }

    public void setUpperText(String upperText) {
        this.upperText = upperText;
    }

    public String getLowerText() {
        return lowerText;
    }

    public void setLowerText(String lowerText) {
        this.lowerText = lowerText;
    }
}
