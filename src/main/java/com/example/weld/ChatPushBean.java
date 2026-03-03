package com.example.weld;

import jakarta.enterprise.context.*;
import jakarta.enterprise.event.*;

@ApplicationScoped
public class ChatPushBean {

   public void onChatMessage(@Observes ChatMessageEvent event) {
      System.out.println("ChatPushBean: event = " + event);
   }
}
