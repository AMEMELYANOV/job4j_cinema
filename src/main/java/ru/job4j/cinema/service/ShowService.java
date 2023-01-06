package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Show;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Сервис сеансов, логика работы с сеансами
 * @see ru.job4j.cinema.model.Show
 * @author Alexander Emelyanov
 * @version 1.0
 */
public interface ShowService {

    /**
     * Возвращает список всех сеансов
     *
     * @return список всех сеансов
     */
    List<Show> findAll();

    /**
     * Возвращает сеанс по идентификатору.
     *
     * @param id идентификатор сеанса
     * @return show при успешном нахождении
     * @exception NoSuchElementException, если show отсутствует.
     */
    Show findById(int id);

    /**
     * Выполняет сохранение сеанса. При успешном сохранении возвращает объект сеанса,
     * у которого проинициализировано id. Иначе возвращает Optional.empty()
     *
     * @param show сохраняемый сеанс
     * @return Optional.of(session) при успешном сохранении, иначе Optional.empty()
     */
    Show save(Show show);

    /**
     * ????
     * Выполняет обновление объекта сеанс.
     *
     * @param show объект сеанс
     */
    void update(Show show);

    /**
     * Выполняет удаление сеанса по идентификатору. При успешном
     * удалении возвращает true, при неудачном false.
     *
     * @param id идентификатор сеанса
     * @return {@code true} при успешном удалении сеанса, иначе {@code false}
     */
    boolean deleteById(int id);

    /**
     * Возвращает список рядов по идентификатору сеанса.
     *
     * @param showId идентификатор сеанса
     * @return {@code List<Integer>} рядов в зале
     */
    List<Integer> getRows(int showId);

    /**
     * Возвращает список мест в ряде по идентификатору сеанса.
     *
     * @param showId идентификатор сеанса
     * @param posRow номер ряда
     * @return {@code List<Integer>} мест в ряде
     */
    List<Integer> getCells(int showId, int posRow);
}
