package chkyass.entity;


import org.springframework.core.annotation.Order;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToMany(mappedBy = "rooms", cascade = {CascadeType.ALL})
    List<User> users = new ArrayList<>();


    @OneToMany(mappedBy = "room", cascade = {CascadeType.ALL}, orphanRemoval = true)
    @Size(max = 20)
    @OrderBy("timestamp")
    List<Message> messages = new ArrayList<>();

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Room() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
