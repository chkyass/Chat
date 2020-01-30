package chkyass.entity;

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
    private long id;

    @NotBlank
    private String user;

    private String message;

    @CreationTimestamp
    private LocalDateTime timestamp;

    // mandatory for jackson Json conversion
    public Message() {
    }

    public Message(Message copy) {
        this.user = copy.user;
        this.message = copy.message;
    }

    public Message(String user, String message) {
        this.user = user;
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
