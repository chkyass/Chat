package chkyass.entity;

import javax.persistence.*;
import java.util.Objects;

@Embeddable
@Table(name = "AUTHORITIES")
public class Authority {
    //@Column(name = "username")
    //private String username;

    @Column(name = "AUTHORITY")
    private String role;

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    User user;

    public Authority(String username, String role) {
        //this.username = username;
        this.role = role;
    }*/

    public Authority(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authority authority = (Authority) o;
        return role.equals(authority.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(role);
    }

    public Authority() {
    }

    /*public String getUsername() {
        return username;
    }*/

    /*public void setUsername(String username) {
        this.username = username;
    }*/

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authority authority = (Authority) o;
        return username.equals(authority.username) &&
                role.equals(authority.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, role);
    }*/
}
