package chkyass.service;

import chkyass.entity.Authority;
import chkyass.entity.Message;
import chkyass.entity.User;
import chkyass.repository.MessageRepository;
import chkyass.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class UserServices {

    @Autowired
    private UsersRepository users;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessageRepository messageRepository;

    private long nb_users = 0;


    synchronized public void increaseUserCount() {
        nb_users++;
    }

    synchronized public void decreaseUserCount() {
        nb_users--;
    }

    public long numberOfAuthenticatedUsers() {
        return nb_users;
    }

    @Transactional
    public List<Message> history() {
        Optional<List<Message>> messages = messageRepository.findFirst20ByOrderByTimestampDesc();
        if(messages.isPresent())
            return messages.get();

        return new ArrayList<>();
    }

    @Transactional
    public void clearUserMessages(String user) {
        messageRepository.deleteUserMessages(user);
    }

    @Transactional
    public void persistMessage(Message message) {
        if(users.existsById(message.getUser()))
            messageRepository.save(message);
    }


    @Transactional
    public boolean register(HttpServletRequest request, String username, String password) throws ServletException {
        if(users.existsById(username))
            return false;

        User user = new User(username, passwordEncoder.encode(password), true);
        user.addRole(new Authority("ROLE_USER"));
        users.save(user);
        //To force application of the save before to login the user
        users.flush();

        request.login(username, password);
        return true;
    }

}
