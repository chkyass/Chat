package chkyass.entity;

import javax.persistence.*;

@Entity
@Table(name = "users_rooms")
public class UserRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "room_id")
    private long roomId;

    public UserRoom() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long room_id) {
        this.roomId = room_id;
    }

    public UserRoom(String userId, long roomId) {
        this.userId = userId;
        this.roomId = roomId;
    }
}
