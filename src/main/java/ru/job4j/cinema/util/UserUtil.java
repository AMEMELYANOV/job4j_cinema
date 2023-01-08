package ru.job4j.cinema.util;

import ru.job4j.cinema.model.User;

import javax.servlet.http.HttpServletRequest;

public final class UserUtil {

    private UserUtil() {
        throw new AssertionError();
    }

    public static User getSessionUser(HttpServletRequest req) {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            user = new User();
//            user.setEmail("Guest");
        }
        return user;
    }
}
