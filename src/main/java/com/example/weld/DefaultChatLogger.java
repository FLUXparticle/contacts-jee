package com.example.weld;

import jakarta.enterprise.context.*;
import org.slf4j.*;

@ApplicationScoped
public class DefaultChatLogger implements ChatLogger {

   private static final Logger LOGGER = 
                   LoggerFactory.getLogger(DefaultChatLogger.class);

   @Override
   public void logMessage(ChatMessageEvent event) {
       LOGGER.info("Nachricht: {}", event);
   }

}
