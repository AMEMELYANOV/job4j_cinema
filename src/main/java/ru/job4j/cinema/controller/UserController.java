package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import ru.job4j.cinema.service.UserService;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
}