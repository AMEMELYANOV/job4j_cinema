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
    /**
     * Количество рядов в кинозале
     */
    @Value("${show.rows}")
    private int rows;
    /**
     * Количество мест в ряде кинозала
     */
    @Value("${show.cells}")
    private int cells;
    /**
     * Объект для доступа к методам слоя ShowRepository
     */
    private final ShowRepository showRepository;
    /**
     * Объект для доступа к методам слоя TicketRepository
     */
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

    /**
     * Возвращает список всех сеансов.
     *
     * @return {@code List<Show>} - список всех сеансов
     */
    @Override
    public List<Show> findAll() {
       return showRepository.findAll();
    }

    /**
     * Выполняет поиск сеанс по идентификатору. При успешном нахождении возвращает
     * сеанс, иначе выбрасывает исключение.
     *
     * @param id идентификатор сеанса
     * @return сеанса при успешном нахождении
     * @exception NoSuchElementException, если сеанс не найден
     */
    @Override
    public Show findById(int id) {
        return showRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(
                        String.format("Сеанс c id = %d не найден", id)));
    }

    /**
     * Выполняет сохранение сеанса. При успешном сохранении возвращает
     * сеанс, иначе выбрасывается исключение.
     *
     * @param show сохраняемый сеанс
     * @return сеанс при успешном нахождении
     * @exception IllegalArgumentException, если сохранение сеанса не произошло
     */
    @Override
    public Show save(Show show) {
        return showRepository.save(show).orElseThrow(
                () -> new IllegalArgumentException("Сеанс не сохранен"));
    }

    /**
     * Выполняет обновление сеанса.
     *
     * @param show обновляемый сеанс
     */
    @Override
    public void update(Show show) {
        showRepository.update(show);
    }

    /**
     * Выполняет удаление сеанса по идентификатору. При успешном удалении
     * сеанса возвращает true, иначе выбрасывается исключение.
     *
     * @param id идентификатор сеанса
     * @return true при успешном удалении
     * @exception NoSuchElementException, если сеанс не найден
     */
    @Override
    public boolean deleteById(int id) {
        boolean result = showRepository.deleteById(id);
        if (!result) {
            throw new NoSuchElementException(
                    String.format("Сеанс c id = %d не найден", id));
        }
        return true;
    }

    /**
     * Выполняет расчет списка рядов в зале со свободными местами
     * по переданному идентификатору сеанса. Для расчета используется
     * вспомогательный метод {@link ImplShowService#getFreeTicketMap(int)}.
     * Возвращает список рядов со свободными местами.
     *
     * @param id идентификатор сеанса
     * @return {@code List<Integer>} список рядов в зале со
     * свободными местами
     */
    @Override
    public List<Integer> getRows(int id) {
        Map<Integer, List<Integer>> freeTickets = getFreeTicketMap(id);
        List<Integer> rows = new ArrayList<>();
        for (Map.Entry<Integer, List<Integer>> entry : freeTickets.entrySet()) {
            if (entry.getValue().size() > 0) {
                rows.add(entry.getKey());
            }
        }
        return rows;
    }

    /**
     * Выполняет расчет списка свободных мест в ряде по
     * идентификаторам сеанса и номеру ряда.
     * Для расчета используется вспомогательный метод
     * {@link ImplShowService#getFreeTicketMap(int)}.
     * Возвращает список свободных мест в ряде.
     *
     * @param id идентификатор сеанса
     * @param posRow номер ряда
     * @return {@code List<Integer>} - список свободных мест в ряде
     */
    @Override
    public List<Integer> getCells(int id, int posRow) {
        Map<Integer, List<Integer>> freeTickets = getFreeTicketMap(id);
        return freeTickets.get(posRow);
    }

    /**
     * Возвращает сгенерированное отображение свободных мест в сеансе
     * по идентификатору.
     *
     * @param id идентификатор сеанса
     * @return {@code Map<Integer, List<Integer>>} - отображение, где ключ - номер ряда,
     * список - номера мест в ряде
     */
    private Map<Integer, List<Integer>> getFreeTicketMap(int id) {
        Map<Integer, List<Integer>> freeTickets = new HashMap<>();
        for (int i = 1; i <= rows; i++) {
            freeTickets.put(i, new ArrayList<>());
            for (int j = 1; j <= cells; j++) {
                freeTickets.get(i).add(j);
            }
        }
        List<Ticket> tickets = ticketRepository.findAllTicketsByShowId(id);
        for (Ticket ticket : tickets) {
            freeTickets.get(ticket.getPosRow()).remove(ticket.getCell() - 1);
        }
        return freeTickets;
    }
}
