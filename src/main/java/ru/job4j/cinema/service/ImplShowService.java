package ru.job4j.cinema.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Show;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.ShowRepository;
import ru.job4j.cinema.repository.TicketRepository;

import java.util.*;

/**
 * Реализация сервиса по работе с сеансами
 * @see ru.job4j.cinema.model.Show
 * @author Alexander Emelyanov
 * @version 1.0
 */
@Service
public class ImplShowService implements ShowService {

    @Value("${show.rows}")
    private int rows;
    @Value("${show.cells}")
    private int cells;
    private final ShowRepository showRepository;
    private final TicketRepository ticketRepository;

    /**
     * Конструктор класса.
     *
     * @param showRepository объект для доступа к методам слоя ShowRepository
     * @param ticketRepository объект для доступа к методам слоя TicketRepository
     */
    public ImplShowService(ShowRepository showRepository, TicketRepository ticketRepository) {
        this.showRepository = showRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public List<Show> findAll() {
       return showRepository.findAll();
    }

    /**
     * Выполняет поиск сеанса по идентификатору. При успешном нахождении возвращает
     * Optional с объектом сеанса. Иначе возвращает Optional.empty().
     *
     * @param id идентификатор сеанса
     * @return Optional.of(show) при успешном нахождении, иначе Optional.empty()
     */
    @Override
    public Show findById(int id) {
        return showRepository.findById(id).get();
    }

    @Override
    public Show save(Show show) {
        return null;
    }

    @Override
    public void update(Show show) {
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }

    /**
     * Возвращает список рядов по идентификатору сеанса.
     *
     * @param showId идентификатор сеанса
     * @return {@code List<Integer>} рядов в зале
     */
    @Override
    public List<Integer> getRows(int showId) {
        Map<Integer, List<Integer>> freeTickets = getFreeTicketMap(showId);
        List<Integer> rows = new ArrayList<>();
        for (Map.Entry<Integer, List<Integer>> entry : freeTickets.entrySet()) {
            if (entry.getValue().size() > 0) {
                rows.add(entry.getKey());
            }
        }
        return rows;
    }

    /**
     * Возвращает список мест в ряде по идентификатору сеанса.
     *
     * @param showId идентификатор сеанса
     * @param posRow номер ряда
     * @return {@code List<Integer>} мест в ряде
     */
    @Override
    public List<Integer> getCells(int showId, int posRow) {
        Map<Integer, List<Integer>> freeTickets = getFreeTicketMap(showId);
        return freeTickets.get(posRow);
    }

    /**
     * Возвращает сгенерированное отображение свободных мест в сеансе по id.
     *
     * @param showId идентификатор сеанса
     * @return {@code Map<Integer, List<Integer>>} ключ - номер ряда, список - номера мест в ряде
     */
    private Map<Integer, List<Integer>> getFreeTicketMap(int showId) {
        Map<Integer, List<Integer>> freeTickets = new HashMap<>();
        for (int i = 1; i <= rows; i++) {
            freeTickets.put(i, new ArrayList<>());
            for (int j = 1; j <= cells; j++) {
                freeTickets.get(i).add(j);
            }
        }
        List<Ticket> tickets = ticketRepository.findAllTicketsByShowId(showId);
        for (Ticket ticket : tickets) {
            freeTickets.get(ticket.getPosRow()).remove(ticket.getCell() - 1);
        }
        return freeTickets;
    }
}
