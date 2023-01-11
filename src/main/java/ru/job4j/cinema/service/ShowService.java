package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Show;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Сервис сеансов, логика работы с сеансами
 * @see Show
 * @author Alexander Emelyanov
 * @version 1.0
 */
public interface ShowService {

    /**
     * Возвращает список всех сеансов.
     *
     * @return {@code List<Show>} - список всех сеансов
     */
    List<Show> findAll();

    /**
     * Выполняет поиск сеанс по идентификатору. При успешном нахождении возвращает
     * сеанс, иначе выбрасывает исключение.
     *
     * @param id идентификатор сеанса
     * @return сеанса при успешном нахождении
     * @exception NoSuchElementException, если сеанс не найден
     */
    Show findById(int id);

    /**
     * Выполняет сохранение сеанса. При успешном сохранении возвращает
     * сеанс, иначе выбрасывается исключение.
     *
     * @param show сохраняемый сеанс
     * @return сеанс при успешном нахождении
     * @exception IllegalArgumentException, если сохранение сеанса не произошло
     */
    Show save(Show show);

    /**
     * Выполняет обновление сеанса.
     *
     * @param show обновляемый сеанс
     */
    void update(Show show);

    /**
     * Выполняет удаление сеанса по идентификатору. При успешном удалении
     * сеанса возвращает true, иначе выбрасывается исключение.
     *
     * @param id идентификатор сеанса
     * @return true при успешном удалении
     * @exception NoSuchElementException, если сеанс не найден
     */
    boolean deleteById(int id);

    /**
     * Выполняет расчет списка рядов в зале со свободными местами
     * по переданному идентификатору сеанса.
     * Возвращает список рядов со свободными местами.
     *
     * @param id идентификатор сеанса
     * @return {@code List<Integer>} список рядов в зале со
     * свободными местами
     */
    List<Integer> getRows(int id);

    /**
     * Выполняет расчет списка свободных мест в ряде по
     * идентификаторам сеанса и номеру ряда.
     * Возвращает список свободных мест в ряде.
     *
     * @param id идентификатор сеанса
     * @param posRow номер ряда
     * @return {@code List<Integer>} - список свободных мест в ряде
     */
    List<Integer> getCells(int id, int posRow);
}
