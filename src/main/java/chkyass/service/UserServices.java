package chkyass.service;

import chkyass.entity.Authority;
import chkyass.entity.User;
import chkyass.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Service
public class UserServices {

    @Autowired
    UsersRepository users;

    @Autowired
    PasswordEncoder passwordEncoder;

    public boolean register(HttpServletRequest request, String username, String password) throws ServletException {
        if(users.existsById(username))
            return false;
        User user = new User(username, passwordEncoder.encode(password), true);
        user.addRole(new Authority("ROLE_USER"));
        System.out.println("before user save");
        users.save(user);
        System.out.println("before request call");
        request.login(username, password);
        System.out.println("after request call");
        return true;
    }

}
