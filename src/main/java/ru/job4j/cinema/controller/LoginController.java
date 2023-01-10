package ru.job4j.cinema.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.UserService;
import ru.job4j.cinema.util.UserUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.NoSuchElementException;

@Controller
@Slf4j
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model,
                            HttpServletRequest request) {
        String errorMessage = null;
        if (error != null) {
            errorMessage = "Имя аккаунта или пароль введены неправильно!";
        }
        if (logout != null) {
            errorMessage = "Вы вышли!";
        }
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("user", UserUtil.getSessionUser(request));
        log.info("Method {} run", "loginPage");
        return "user/login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, HttpServletRequest req) {
        try {
            if (userService.validateUserLogin(user)) {
                User userFromDB = userService.findUserByEmail(user.getEmail());
                HttpSession session = req.getSession();
                session.setAttribute("user", userFromDB);
            }
        } catch (NoSuchElementException e) {
            return "redirect:/login?error=true";
        }

//        log.info("Method {} run", "loginUser");
        return "redirect:/shows";
    }

    @GetMapping(value = "/logout")
    public String logoutPage(HttpSession session) {
        session.invalidate();

        log.info("Method {} run", "logoutPage");
        return "redirect:/login?logout=true";
    }
}
