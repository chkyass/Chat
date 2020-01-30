package chkyass.entity;

import javax.persistence.*;
import java.util.Objects;

@Embeddable
@Table(name = "AUTHORITIES")
public class Authority {

    @Column(name = "AUTHORITY")
    private String role;

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
