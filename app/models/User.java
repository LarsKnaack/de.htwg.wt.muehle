package models;

/**
 * Created by Lars on 19.01.2017.
 */
public class User {
    public String email;
    public String password;

    public User() {
    }

    public User(final String email, final String password) {
        this.email = email;
        this.password = password;
    }
}
