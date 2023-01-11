package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Ticket;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Сервис билетов, логика работы с билетами
 * @see ru.job4j.cinema.model.Ticket
 * @author Alexander Emelyanov
 * @version 1.0
 */
public interface TicketService {

    /**
     * Возвращает список всех билетов
     *
     * @return {@code List<Ticket>} - список всех билетов
     */
    List<Ticket> findAll();

    /**
     * Выполняет поиск билета по идентификатору. При успешном нахождении возвращает
     * билет, иначе выбрасывает исключение.
     *
     * @param id идентификатор билета
     * @return билет при успешном нахождении
     * @exception NoSuchElementException, если билет не найден
     */
    Ticket findById(int id);

    /**
     * Выполняет сохранение билета. При успешном сохранении возвращает
     * сохраненный билет, иначе выбрасывается исключение.
     *
     * @param ticket сохраняемый билет
     * @return билет при успешном сохранении
     * @exception IllegalArgumentException, если сохранение билета не произошло
     */
    Ticket save(Ticket ticket);

    /**
     * Выполняет обновление билета.
     *
     * @param ticket обновляемый билет
     */
    void update(Ticket ticket);

    /**
     * Выполняет удаление билета по идентификатору. При успешном удалении
     * билета возвращает true, иначе выбрасывается исключение.
     *
     * @param id идентификатор билета
     * @return true при успешном удалении
     * @exception NoSuchElementException, если билет не найден
     */
    boolean deleteById(int id);
}
