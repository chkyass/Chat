package chkyass.listener;

import chkyass.service.UserServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.stereotype.Component;

@Component
public class LogoutListener implements ApplicationListener<SessionDestroyedEvent> {
    @Autowired
    SimpMessagingTemplate template;

    @Autowired
    UserServices userServices;

    Logger logger = LoggerFactory.getLogger(LogoutListener.class);


    @Override
    public void onApplicationEvent(SessionDestroyedEvent sessionDestroyedEvent) {
        userServices.decreaseUserCount();
        template.convertAndSend("/topic/numberOfUsers", userServices.numberOfAuthenticatedUsers());
        logger.info("User logout");
    }
}
