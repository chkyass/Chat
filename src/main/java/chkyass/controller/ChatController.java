package chkyass.controller;

import chkyass.model.Message;
import chkyass.model.MessageForward;
import chkyass.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ChatController {

    @Autowired
    UserServices service;


    /**
     * Used to forward a message to everyone
     * @param message
     * @return message to be forwarded
     */
    @MessageMapping("/message")
    @SendTo("/room/messages")
    public MessageForward forwardToAll(Message message) {
        return new MessageForward(message, service.getOnlineUsers());
    }

    /**
     * login page
     */
    @GetMapping(value = {"/"})
    public String root() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication instanceof AnonymousAuthenticationToken);
        if(authentication instanceof AnonymousAuthenticationToken)
            return "index";
        return "chat";
    }


    /**
     * chat page
     */
    @GetMapping("/chat")
    public String chat(Model model) {
        return "chat";
    }

}
