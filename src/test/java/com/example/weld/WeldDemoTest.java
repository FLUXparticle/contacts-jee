package com.example.weld;

import jakarta.inject.*;
import org.jboss.weld.junit5.auto.*;
import org.junit.jupiter.api.*;

@EnableAutoWeld
class WeldDemoTest {

    @Inject
    App app;

    @Test
    void runCdiDemo() {
        app.run();
    }
}
