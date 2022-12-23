package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.cinema.service.ImplShowService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class TicketController {
    private final ImplShowService showService;

    public TicketController(ImplShowService showService) {
        this.showService = showService;
    }

    @PostMapping("/bookTicket")
    public String bookTicket(@RequestParam(value = "cell") Integer cell,
                        Model model, HttpServletRequest req) {
        HttpSession session = req.getSession();
        session.setAttribute("cell", cell);
        model.addAttribute("show", session.getAttribute("show"));
        model.addAttribute("posRow", session.getAttribute("posRow"));
        model.addAttribute("cell", session.getAttribute("cell"));
        return "bookTicket";
    }

    @PostMapping("/cancelBuyTicket")
    public String cancelBuyTicket() {
        return "redirect:/index";
    }

    @PostMapping("/confirmBuyTicket")
    public String confirmBuyTicket(Model model, HttpServletRequest req) {
        HttpSession session = req.getSession();
        model.addAttribute("show", session.getAttribute("show"));
        model.addAttribute("posRow", session.getAttribute("posRow"));
        model.addAttribute("cell", session.getAttribute("cell"));
        return "/successful";
    }
}