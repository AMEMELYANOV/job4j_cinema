package ru.job4j.cinema.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.UserService;
import ru.job4j.cinema.util.UserUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@Slf4j
@RequestMapping("/registration")
public class RegController {

    private final UserService userService;

    public RegController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public String regSave(@Valid @ModelAttribute User user, Errors errors,
                          @RequestParam String repassword) {
        if (errors.hasErrors()) {
            return "user/registration";
        }
        User userFromDB = userService.findUserByEmail(user.getEmail());
        if (userFromDB != null) {
            return "redirect:/user/registration?account=true";
        }
        if (!user.getPassword().equals(repassword)) {
            return "redirect:/user/registration?password=true";
        }
        userService.save(user);

//        log.info("Method {} run", "regSave");
        return "redirect:/user/login";
    }

    @GetMapping
    public String regPage(@RequestParam(value = "password", required = false) String password,
                          @RequestParam(value = "account", required = false) String account,
                          Model model,
                          HttpServletRequest request) {
        String errorMessage = null;
        if (password != null) {
            errorMessage = "Пароли должны совпадать!";
        }
        if (account != null) {
            errorMessage = "Аккаунт уже существует!";
        }
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("user", UserUtil.getSessionUser(request));
//        log.info("Method {} run", "regPage");
        return "user/registration";
    }
}