package com.example.weld;

import jakarta.enterprise.context.*;
import jakarta.enterprise.event.*;
import org.slf4j.*;


@ApplicationScoped
public class ChatMessageSaver {

   private static final Logger LOGGER =
                    LoggerFactory.getLogger(ChatMessageSaver.class);

   public void saveMessage(@Observes ChatMessageEvent event) {
       LOGGER.info("Nachricht: {}", event);
   }
}
