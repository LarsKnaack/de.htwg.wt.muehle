package services;

import models.User;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Lars on 19.01.2017.
 */
public class UserService {

    private static List<User> users = new LinkedList<>();
    private static final UserService instance = new UserService();

    private UserService() {
    }

    public static Optional<User> authenticate(User user) {
        Optional<User> tmp = getUser(user);
        if (tmp.isPresent()) {
            String password = tmp.get().password;
            if (password.equals(user.password)) {
                return tmp;
            }
        }
        return Optional.empty();
    }

    public static Optional<User> getUser(User user) {
        return users.stream().filter(tmp -> tmp.equals(user)).findFirst();
    }

    public static Optional<User> getUserByMail(String mail) {
        return users.stream().filter(tmp -> tmp.email.equals(mail)).findFirst();
    }

    public static UserService getInstance() {
        return instance;
    }

    public void addUser(User account) {
        users.add(account);
    }
}
