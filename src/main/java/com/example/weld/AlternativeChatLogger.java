package com.example.weld;

import jakarta.enterprise.context.*;
import jakarta.enterprise.inject.*;
import org.slf4j.*;

@ApplicationScoped
@Alternative
public class AlternativeChatLogger implements ChatLogger {

   private static final Logger LOGGER = 
                   LoggerFactory.getLogger(AlternativeChatLogger.class);

   @Override
   public void logMessage(ChatMessageEvent event) {
       LOGGER.info("Nachricht: {}", event);
   }

}
