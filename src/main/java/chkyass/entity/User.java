package chkyass.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Entity
@Table(name = "USERS")
public class User {
    @Id
    @NotBlank
    @Column(length = 20)
    private String username;

    @Column
    private String password;

    @Column
    private boolean enabled;

    @ElementCollection
    @CollectionTable(name ="AUTHORITIES",joinColumns = @JoinColumn(name = "USERNAME"))
    private Set<Authority> roles = new HashSet<>();

    /*@ManyToMany(cascade = {CascadeType.ALL})
    /@JoinTable(name = "users_rooms", joinColumns = @JoinColumn(name = "user_id")
    /
    /                                ,inverseJoinColumns = @JoinColumn(name = "room_id"))
    @Transient
    private List<Room> rooms = new ArrayList<>();*/

    public User(String username, String password, boolean enabled) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    public void addRole(Authority role) {
        roles.add(role);
    }

    public void removeRole(Authority authority) {
        roles.remove(authority);
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    /*public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }*/

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Authority> getRoles() {
        return roles;
    }

    public void setRoles(Set<Authority> roles) {
        this.roles = roles;
    }

    /*public void addRoom(Room r) {
        rooms.add(r);
    }*/

    public User(@NotBlank String username, String password, boolean enabled, Set<Authority> roles) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.roles = roles;
    }
}
