package chkyass.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * WebSocket message entity
 */
@Entity
@Table(name = "MESSAGES")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private long id;

    @NotBlank
    @Column(name = "username")
    private String username;

    @Column(length = 2000)
    private String message;

    @Column(name = "room_id")
    @JsonIgnore
    private long roomId;

    @JsonIgnore
    @CreationTimestamp
    private LocalDateTime timestamp;

    // mandatory for jackson Json conversion
    public Message() {
    }

    public Message(Message copy) {
        this.username = copy.username;
        this.message = copy.message;
    }

    public Message(String username, String message) {
        this.username = username;
        this.message = message;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String user) {
        this.username = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
