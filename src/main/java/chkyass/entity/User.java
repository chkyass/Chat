package chkyass.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Entity
@Table(name = "USERS")
public class User {
    @Id
    @NotBlank
    private String username;

    private String password;

    private boolean enabled;

    @ElementCollection
    @CollectionTable(name ="AUTHORITIES",joinColumns = @JoinColumn(name = "USERNAME"))
    private Set<Authority> roles = new HashSet<>();

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

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Authority> getRoles() {
        return roles;
    }

    public void setRoles(Set<Authority> roles) {
        this.roles = roles;
    }

    public User(@NotBlank String username, String password, boolean enabled, Set<Authority> roles) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.roles = roles;
    }
}
