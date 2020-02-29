package chkyass.service;

import chkyass.entity.*;
import chkyass.repository.MessageRepository;
import chkyass.repository.RoomRepository;
import chkyass.repository.UserRoomRepository;
import chkyass.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;


@Service
@EnableAsync
public class UserServices {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRoomRepository userRoomRepository;

    @Autowired
    private JavaMailSender javaMailSender;


    private Hashtable<String, Long> user_room = new Hashtable(){{
        put("yacine", new Long(1));
        put("admin", new Long (1));
    }};

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

    private Logger logger = LoggerFactory.getLogger(UserServices.class);

    public Long getUserRoom(String username) {
        logger.info("Getting Room: " + username);
        Long room_id = (Long) user_room.get(username);
        if(room_id == null) {
            logger.info("Cache fail: get rooms: " + username);
            room_id = userRoomRepository.findByUserId(username).get().get(0).getRoomId();
            user_room.put(username, room_id);
        }
        logger.info("Room id: " + room_id);
        return room_id;
    }

    public List<Message> roomHistory(String username) {
        logger.info("Query user message history");
        return messageRepository.findFirst50ByRoomIdOrderByTimestampDesc(getUserRoom(username)).orElse(new ArrayList<Message>());
    }

    public List<String> usersRoom(String username) {
        return userRoomRepository.findByRoomId(getUserRoom(username))
                .orElse(new ArrayList<>())
                .stream()
                .map(u -> u.getUserId())
                .collect(Collectors.toList());
    }

    @Transactional
    public void persistMessage(Message message) {
        if(usersRepository.existsById(message.getUsername())) {
            logger.info("Persist Message");
            message.setRoomId(getUserRoom(message.getUsername()));
            messageRepository.saveAndFlush(message);
        } else {
            logger.info("Trying to persist message of not existing User: " + message.getUsername());
        }
    }

    @Async
    public void sendEmail(String to, String subj) {
        logger.info("Sending email from Thread: "+ Thread.currentThread().getName());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subj);
        message.setText("");

        javaMailSender.send(message);
    }


    @Transactional
    public boolean register(HttpServletRequest request, String username, String password) throws ServletException {
        logger.info("Checking user existence");
        if(usersRepository.findById(username).isPresent())
            return false;

        logger.info("Create User");
        User user = new User(username, passwordEncoder.encode(password), true);
        user.addRole(new Authority("ROLE_USER"));

        logger.info("Saving user");
        usersRepository.save(user);

        logger.info("Create Room");
        Room room = roomRepository.save(new Room(username));

        logger.info("Room created");
        user_room.put(username, room.getId());

        logger.info("Update join table");
        userRoomRepository.save(new UserRoom(username, room.getId()));

        logger.info("Flushing");
        usersRepository.flush();
        userRoomRepository.flush();
        roomRepository.flush();

        logger.info("Request Login");
        request.login(username, password);
        logger.info("finishing registration");
        return true;
    }

    @Transactional
    public Map<Long, List<String>> getRooms(){
        Map<Long, List<String >> rooms_to_users = new HashMap();
        for(UserRoom ur : userRoomRepository.findAll()) {
            List l = rooms_to_users.getOrDefault(ur.getRoomId(), new ArrayList<>());
            l.add(ur.getUserId());
            rooms_to_users.put(ur.getRoomId(), l);
        }

        return rooms_to_users;
    }

    @Transactional
    public List<Message> setRoom(String username, long id) {
        user_room.put(username, id);
        return messageRepository.findFirst50ByRoomIdOrderByTimestampDesc(id).orElse(new ArrayList<>());
    }

    @Transactional
    public void changeUserRoom(String username, long id) {
        user_room.put(username, id);
        userRoomRepository.updateRoomIdByUsername(username, id);
    }
}
