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

@Controller
public class ShowController {
    private final ImplShowService showService;

    public ShowController(ImplShowService showService) {
        this.showService = showService;
    }

    @GetMapping("/shows")
    public String getShowRow(Model model, HttpServletRequest req) {
        model.addAttribute("user", UserUtil.getSessionUser(req));
        model.addAttribute("shows", showService.findAll());
        return "show/shows";
    }

    @PostMapping("/showRow")
    public String getPostShowRow(@RequestParam(value = "showId") int showId,
                        Model model, HttpServletRequest req) {
        Show showFromDB = showService.findById(showId);
        model.addAttribute("show", showFromDB);
        List<Integer> rows = showService.getRows(showId);
        model.addAttribute("rows", List.of(1, 2, 3, 4, 5, 6, 7));
        HttpSession session = req.getSession();
        session.setAttribute("show", showFromDB);
        model.addAttribute("user", UserUtil.getSessionUser(req));

        return "show/showRow";
    }

    @PostMapping("/showCell")
    public String setShowRow(@RequestParam(value = "posRow") Integer posRow,
                        Model model, HttpServletRequest req) {
        HttpSession session = req.getSession();
        session.setAttribute("posRow", posRow);
        Show show = (Show) session.getAttribute("show");
        model.addAttribute("show", show);
        model.addAttribute("cells", showService.getCells(show.getId(), posRow));
        model.addAttribute("user", UserUtil.getSessionUser(req));
        return "show/showCell";
    }
}