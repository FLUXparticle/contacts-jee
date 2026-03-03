package com.example.weld;

import jakarta.inject.*;
import org.jboss.weld.junit5.auto.*;
import org.junit.jupiter.api.*;

@EnableAutoWeld
@AddPackages(value = App.class)
@EnableAlternatives(AlternativeChatLogger.class)
class WeldDemoTest {

    @Inject
    App app;

    @Inject
    ChatService service;

//    @Inject
//    TestChatLogger chatLogger;

    @Test
    void runCdiDemo() {
        app.run();

        System.out.println("service = " + service.getClass().getName());
        service.test();
        service.sendMessage("Hi", "me");

//        List<String> messages = chatLogger.getLoggedMessages();
//        System.out.println("messages = " + messages);
    }
}
