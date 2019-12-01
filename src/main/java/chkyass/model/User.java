package chkyass.model;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class User {

    @NotBlank(message = "username cannot be empty")
    private String name;

    public User(@NotBlank String username) {

        this.name = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if( obj == this) return true;
        if(! (obj instanceof User)) return false;

        return ((User) obj).getName().equals(name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
