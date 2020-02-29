package chkyass.controller;

import chkyass.entity.Message;
import chkyass.service.UserServices;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Map;


@Controller
public class ChatController {
    Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    UserServices service;

    @Autowired
    SimpMessagingTemplate template;

    @Autowired
    Environment env;

    /**
     * Used to forward a message to everyone
     * @param message
     * @return message to be forwarded
     */
    @MessageMapping("/message")
    @Transactional
    public Message forwardToAll(Message message) {
        service.persistMessage(message);
        template.convertAndSend("/topic/"+message.getRoomId(), message);
        return message;
    }


    /**
     * login page
     */
    @GetMapping(value = {"/"})
    public String root(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof AnonymousAuthenticationToken)
            return "index";

        if(request.isUserInRole("ADMIN"))
            return "admin";

        return "chat";
    }


    /**
     * register
     */
    @PostMapping("/register")
    @ResponseBody
    public String register(HttpServletRequest request, @RequestParam(name = "user") String username, @RequestParam(name = "pass") String pass) {
        logger.info("User register");
        JSONObject jsonObject = new JSONObject();
        try {
            if (pass.isEmpty())
                jsonObject.put("message", "Empty password not allowed ");
            else if (!service.register(request, username, pass))
                jsonObject.put("message", "Username already exist");
            else {
                jsonObject.put("redirect", env.getProperty("server.servlet.context-path"));
                logger.info("Before to send email from Thread: " + Thread.currentThread().getName());
                service.sendEmail("ychouaki.chatroom@gmail.com", "Registration:" + username);
            }
        } catch (ServletException e) {
            jsonObject.put("message", e.getMessage());
        }
        finally {
            logger.info("Registration respond to client with: " + jsonObject.toJSONString());
            return jsonObject.toJSONString();
        }
    }

    @GetMapping("/history")
    @ResponseBody
    public List<Message> roomHistory(Principal principal) {
        logger.info("Get history For: " + principal.getName());
        List<Message> messages = service.roomHistory(principal.getName());
        return messages;
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
