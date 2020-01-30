package chkyass.service;

import chkyass.entity.Authority;
import chkyass.entity.Message;
import chkyass.entity.Room;
import chkyass.entity.User;
import chkyass.repository.MessageRepository;
import chkyass.repository.RoomRepository;
import chkyass.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Hashtable;
import java.util.List;


@Service
public class UserServices {

    @Autowired
    private UsersRepository users;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private MessageRepository messageRepository;

    private Hashtable<String, Room> user_room = new Hashtable<>();

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
    public Room getUserRoom(String username) {
        Room room = usersRepository.findById(username).get().getRooms().get(0);
        user_room.put(username, room);
        return room;
    }

    @Transactional
    public List<Message> room_history(String username) {
        if(user_room.containsKey(username))
            return roomRepository.findById(user_room.get(username).getId()).get().getMessages();
        else {
            return roomRepository.findById(getUserRoom(username).getId()).get().getMessages();
        }
    }

    /*@Transactional
    public void clearUserMessages(String user) {
        messageRepository.deleteUserMessages(user);
    }*/

    @Transactional
    public void persistMessage(Message message) {
        if(users.existsById(message.getUser())) {
            message.setRoom(user_room.get(message.getUser()));
            messageRepository.save(message);
        }
    }


    @Transactional
    public boolean register(HttpServletRequest request, String username, String password) throws ServletException {
        if(users.existsById(username))
            return false;

        User user = new User(username, passwordEncoder.encode(password), true);
        user.addRole(new Authority("ROLE_USER"));
        user.addRoom(new Room());
        users.save(user);
        //To force application of the save before to login the user
        users.flush();

        request.login(username, password);
        return true;
    }

}
