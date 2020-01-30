package chkyass.listener;

import chkyass.service.UserServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.web.session.HttpSessionCreatedEvent;
import org.springframework.stereotype.Component;

@Component
public class LoginListener implements ApplicationListener<HttpSessionCreatedEvent> {

    @Autowired
    SimpMessagingTemplate template;

    @Autowired
    UserServices userServices;

    Logger logger = LoggerFactory.getLogger(LoginListener.class);


    @Override
    public void onApplicationEvent(HttpSessionCreatedEvent authenticationSuccessEvent) {
        logger.info("User login");
        userServices.increaseUserCount();
        template.convertAndSend("/topic/numberOfUsers", userServices.numberOfAuthenticatedUsers());
    }
}
