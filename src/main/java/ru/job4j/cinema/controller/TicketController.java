package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.cinema.model.Show;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.ImplTicketService;
import ru.job4j.cinema.util.UserUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class TicketController {
    private final ImplTicketService ticketService;

    public TicketController(ImplTicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/buyTicket")
    public String bookTicket(@RequestParam(value = "cell") Integer cell,
                             Model model, HttpServletRequest req) {
        HttpSession session = req.getSession();
        session.setAttribute("cell", cell);
        model.addAttribute("show", session.getAttribute("show"));
        model.addAttribute("posRow", session.getAttribute("posRow"));
        model.addAttribute("cell", session.getAttribute("cell"));
        model.addAttribute("user", UserUtil.getSessionUser(req));
        return "ticket/buyTicket";
    }

    @PostMapping("/cancelBuyTicket")
    public String cancelBuyTicket() {
        return "redirect:/shows";
    }

    @PostMapping("/confirmBuyTicket")
    public String confirmBuyTicket(Model model, HttpServletRequest req) {
        HttpSession session = req.getSession();
        Ticket ticket = ticketService.save(Ticket
                .builder()
                .user((User) session.getAttribute("user"))
                .show((Show) session.getAttribute("show"))
                .posRow((int) session.getAttribute("posRow"))
                .cell((int) session.getAttribute("cell"))
                .build());
        model.addAttribute("ticket", ticket);
        model.addAttribute("user", session.getAttribute("user"));
        return "ticket/successful";
    }
}