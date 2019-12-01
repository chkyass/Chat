package chkyass.service;

import chkyass.DAO.LogedInUsers;
import chkyass.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
public class UserServices {

    @Autowired
    LogedInUsers users;

    public void login(User user, BindingResult result) {
        if(users.contains(user))
            result.addError(new FieldError("User", "name", "username already exist"));
        else if(user.getName() != null )
            users.add(user);
    }

    public void logout(User user) {
        users.remove(user);
    }

    public int getOnlineUsers() {
        return users.getOnlineCount();
    }
}
