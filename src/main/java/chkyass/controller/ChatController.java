package chkyass.controller;

import chkyass.entity.Message;
import chkyass.service.UserServices;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;


@Controller
public class ChatController {

    Logger logger = LoggerFactory.getLogger(ChatController.class);


    @Autowired
    UserServices service;


    /**
     * Used to forward a message to everyone
     * @param message
     * @return message to be forwarded
     */
    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public Message forwardToAll(Message message) {
        service.persistMessage(message);
        return message;
    }


    /**
     * login page
     */
    @GetMapping(value = {"/"})
    public String root() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof AnonymousAuthenticationToken)
            return "index";
        return "chat";
    }

    /**
     * Remove all messages sent by a user
     *
    @PostMapping("/clear")
    @ResponseBody
    public String clear(@RequestParam String username) {
        service.clearUserMessages(username);
        return "success";
    }*/

    /**
     * register
     */
    @PostMapping("/register")
    @ResponseBody
    public String register(HttpServletRequest request, @RequestParam(name = "user") String username, @RequestParam(name = "pass") String pass) {

        JSONObject jsonObject = new JSONObject();
        try {
            if (pass.isEmpty())
                jsonObject.put("message", "Empty password not allowed ");
            else if (!service.register(request, username, pass))
                jsonObject.put("message", "Username already exist");
            else {
                jsonObject.put("redirect", "/");
            }
        } catch (ServletException e) {
            jsonObject.put("message", e.getMessage());
        }
        finally {
            return jsonObject.toJSONString();
        }
    }

    @GetMapping("/history")
    @ResponseBody
    public List<Message> history(Principal principal) {
        return service.room_history(principal.getName());
    }

    @GetMapping("/online")
    @ResponseBody
    public long online() {
        long n = service.numberOfAuthenticatedUsers();
        logger.info("Number of users online: " + n);
        return n;
    }

    @GetMapping("/username")
    @ResponseBody
    public String username(Principal principal) {
        String username = principal.getName();
        logger.info("Request username: " + username);
        return username;
    }

}
