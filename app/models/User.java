package models;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

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
        final List<String> names = Arrays.asList("a", "b");
        names.stream().sorted(Comparator.comparingInt(String::length)).forEach(it -> System.out.println(it));
    }
}
