package com.example.weld;

import jakarta.annotation.*;
import jakarta.decorator.*;
import jakarta.enterprise.context.*;
import jakarta.inject.*;
import jakarta.interceptor.*;
import org.slf4j.*;

@Decorator
@Dependent
@Priority(Interceptor.Priority.APPLICATION)
public class GreetingDecorator implements ChatLogger {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(GreetingDecorator.class);

    @Inject
    @Delegate
    private ChatLogger delegate;

    @Override
    public void logMessage(ChatMessageEvent event) {
        LOGGER.info("Vorher!");
        delegate.logMessage(event);
        LOGGER.info("Nachher!");
    }
}
