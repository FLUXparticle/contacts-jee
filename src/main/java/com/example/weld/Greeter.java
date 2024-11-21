package com.example.weld;

import jakarta.enterprise.context.*;

@ApplicationScoped
public class Greeter {
    public String greet(String name) {
        return "Hi " + name + " (via CDI)";
    }
}
