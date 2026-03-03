package com.example.weld;

import jakarta.enterprise.context.*;

import java.util.*;

@ApplicationScoped
public class TestChatLogger implements ChatLogger {

    private final List<String> loggedMessages = new ArrayList<>();

    @Override
    public void logMessage(ChatMessageEvent event) {
        loggedMessages.add(event.toString());
    }

    public List<String> getLoggedMessages() {
        return loggedMessages;
    }
}
