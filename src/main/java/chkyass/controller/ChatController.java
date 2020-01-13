package chkyass.controller;

import chkyass.entity.Message;
import chkyass.entity.MessageForward;
import chkyass.service.UserServices;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;


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
        //return new MessageForward(message, service.getOnlineUsers());
        return new MessageForward(message, 2);
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


    /**
     * register
     */
    @PostMapping("/register")
    @ResponseBody
    public String register(HttpServletRequest request,
                           @RequestParam(name = "user") String username,
                           @RequestParam(name = "pass") String pass) {

        JSONObject jsonObject = new JSONObject();
        if (pass.isEmpty()){
            jsonObject.put("status", "error");
            jsonObject.put("message", "Empty password not allowed ");
            return jsonObject.toJSONString();
        }

        try {
            if (!service.register(request, username, pass)) {
                jsonObject.put("status", "error");
                jsonObject.put("message", "Username already exist");
                return jsonObject.toJSONString();
            }
        } catch (ServletException e) {
            jsonObject.put("status", "error");
            jsonObject.put("message", "internal error");
            return jsonObject.toJSONString();
        }

        jsonObject.put("status", "success");
        jsonObject.put("redirect", "/chat");
        return jsonObject.toJSONString();
    }

}
