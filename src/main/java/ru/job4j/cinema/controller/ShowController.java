package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.cinema.model.Show;
import ru.job4j.cinema.service.ImplShowService;
import ru.job4j.cinema.util.UserUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
public class ShowController {
    private final ImplShowService showService;

    public ShowController(ImplShowService showService) {
        this.showService = showService;
    }

    @GetMapping("/showRow{showId}")
    public String getShowRow(@RequestParam(value = "showId") Integer showId,
                        Model model, HttpServletRequest req) {
        Optional<Show> showFromDB = showService.findById(showId);
        model.addAttribute("show", showFromDB
                .orElseThrow(() -> new NoSuchElementException("Сеанс не найден")));
        model.addAttribute("user", UserUtil.getSessionUser(req));

//        model.addAttribute("rows", List.of());
        model.addAttribute("rows", List.of(1, 2, 3, 4, 5, 6, 7));

        HttpSession session = req.getSession();
        session.setAttribute("show", showFromDB.get());

        return "showRow";
    }

    @PostMapping("/showCell")
    public String setShowRow(@RequestParam(value = "posRow") Integer posRow,
                        Model model, HttpServletRequest req) {
        HttpSession session = req.getSession();
        session.setAttribute("posRow", posRow);
        model.addAttribute("show", session.getAttribute("show"));
        model.addAttribute("cells", List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
//        model.addAttribute("cells", List.of());
        return "showCell";
    }
}