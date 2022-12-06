package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.cinema.repository.PostgresSessionRepository;
import ru.job4j.cinema.util.UserUtil;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexControl {
    private final PostgresSessionRepository sessionRepository;

    public IndexControl(PostgresSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @GetMapping("/index")
    public String index(Model model, HttpServletRequest req) {
//        model.addAttribute("user", UserUtil.getSessionUser(req));
        model.addAttribute("sessions", sessionRepository.findAll());
        return "index";
    }
}