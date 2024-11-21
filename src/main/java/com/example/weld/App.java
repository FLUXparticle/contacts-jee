package com.example.weld;

import jakarta.enterprise.context.*;
import jakarta.inject.*;

@ApplicationScoped
public class App {

    private final Greeter greeter;

    @Inject
    public App(Greeter greeter) {
        this.greeter = greeter;
    }

    public void run() {
        System.out.println(greeter.greet("There"));
    }

}
