package chkyass.DAO;

import chkyass.model.User;
import org.springframework.stereotype.Repository;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Repository
public class LogedInUsers {

    private Set<User> users = Collections.synchronizedSet(new HashSet<>());

    public void add(User user) {
        users.add(user);
    }

    public void remove(User user) {
        users.remove(user);
    }

    public boolean contains(User user) {
        return users.contains(user);
    }

    public int getOnlineCount() {
        return users.size();
    }
}
