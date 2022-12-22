package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.cinema.service.ShowService;
import ru.job4j.cinema.util.UserUtil;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    private final ShowService showService;

    public IndexController(ShowService showService) {
        this.showService = showService;
    }

    @GetMapping("/index")
    public String index(Model model, HttpServletRequest req) {
        model.addAttribute("user", UserUtil.getSessionUser(req));
        model.addAttribute("shows", showService.findAll());
        return "index";
    }

}