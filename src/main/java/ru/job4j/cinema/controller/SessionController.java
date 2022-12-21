package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.service.ImplSessionService;
import ru.job4j.cinema.util.UserUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
public class SessionController {
    private final ImplSessionService sessionService;

    public SessionController(ImplSessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/sessionRow{sessionId}")
    public String getSessionHall(@RequestParam(value = "sessionId") Integer sessionId,
                        Model model, HttpServletRequest req) {
        Optional<Session> sessionFromDB = sessionService.findById(sessionId);
        model.addAttribute("sess", sessionFromDB
                .orElseThrow(() -> new NoSuchElementException("Сеанс не найден")));
        model.addAttribute("user", UserUtil.getSessionUser(req));

        model.addAttribute("rows", List.of(1, 2, 3, 4, 5, 6, 7));

        return "sessionRow";
    }

}