package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.cinema.model.Show;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.ImplShowService;
import ru.job4j.cinema.service.ImplTicketService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class TicketController {
    private final ImplTicketService ticketService;

    public TicketController(ImplTicketService ticketService) {
        this.ticketService = ticketService;
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
        Show show = (Show) session.getAttribute("show");
        int posRow = (int) session.getAttribute("posRow");
        int cell = (int) session.getAttribute("cell");

        Optional<Ticket> ticket = ticketService.save(Ticket
                .builder()
                .show(show)
                .posRow(posRow)
                .cell(cell)
                .build());

        model.addAttribute("show", show);
        model.addAttribute("posRow", posRow);
        model.addAttribute("cell", cell);
        return "/successful";
    }
}