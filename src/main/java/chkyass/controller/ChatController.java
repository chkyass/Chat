package chkyass.controller;

import chkyass.model.Message;
import chkyass.model.MessageForward;
import chkyass.model.User;
import chkyass.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;


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
     * Login Submission
     */
    @GetMapping("/login")
    public RedirectView login(@Valid User user, BindingResult result, RedirectAttributes redirectAttributes) {
        service.login(user, result);
        if(result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", result.getFieldError().getDefaultMessage());
            return new RedirectView("/");
        }

        System.out.println("LOGIN " + user.getName());

        // Mandatory to transfer object in request session
        redirectAttributes.addFlashAttribute("username", user.getName());

        return new RedirectView("chat");
    }


    /**
     * Root Page
     */
    @GetMapping("/logout")
    public ModelAndView logout(@RequestParam(value = "user") User user) {
        if(user != null) {
            System.out.println("LOGOUT " + user.getName());
            service.logout(user);
        }
        return new ModelAndView("redirect:/");
    }


    /**
     * login page
     */
    @GetMapping(value = {"/"})
    public String form(Model model) {
        return "form";
    }


    /**
     * chat page
     */
    @GetMapping("/chat")
    public String chat(Model model) {
        if(model.getAttribute("username") == null)
            return "form";
        return "chat";
    }
}
