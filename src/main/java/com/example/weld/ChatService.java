package com.example.weld;

import jakarta.enterprise.context.*;
import jakarta.inject.*;
import org.slf4j.*;

@ApplicationScoped
@Logged
public class ChatService {

   private static final Logger LOGGER = 
                        LoggerFactory.getLogger(ChatService.class);

   @Inject
   private ChatLogger chatLogger;
   //private Event<ChatMessageEvent> chatEvent;

   public void sendMessage(String message, String sender) {
       var event = new ChatMessageEvent(message, sender);
       chatLogger.logMessage(event);
       // chatEvent.fire(event); // Event auslösen
       test();
   }

    public void test() {
       System.out.println("test");
   }

}
