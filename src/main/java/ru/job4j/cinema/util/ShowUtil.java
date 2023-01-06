package ru.job4j.cinema.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import ru.job4j.cinema.model.Show;
import ru.job4j.cinema.model.Ticket;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ShowUtil {

//    @Value("${show.rows}")
//    private static int rows;
//    @Value("${show.cells}")
//    private static int cells;

    private static final Map<Integer, List<Integer>> FREETICKETS = new HashMap<>();

//    public static Map<Integer, List<Integer>> getFreeTickets() {
//        for (int i = 1; i <= rows; i++) {
//            FREETICKETS.put(i, new ArrayList<>());
//            for (int j = 1; j <= cells; j++) {
//                FREETICKETS.get(i).add(j);
//            }
//        }
//        return FREETICKETS;
//    }

//    public int getRows() {
//        return rows;
//    }
//
//    public int getCells() {
//        return cells;
//    }
}
