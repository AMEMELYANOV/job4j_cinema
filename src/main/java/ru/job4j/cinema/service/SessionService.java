package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Session;

import java.util.List;
import java.util.Optional;

/**
 * Сервис сеансов, логика работы с сеансами
 * @see ru.job4j.cinema.model.Session
 * @author Alexander Emelyanov
 * @version 1.0
 */
public interface SessionService {

    /**
     * Возвращает список всех сеансов
     *
     * @return список всех сеансов
     */
    List<Session> findAll();

    /**
     * Выполняет поиск сеанса по идентификатору. При успешном нахождении возвращает
     * Optional с объектом сеанса. Иначе возвращает Optional.empty().
     *
     * @param id идентификатор сеанса
     * @return Optional.of(session) при успешном нахождении, иначе Optional.empty()
     */
    Optional<Session> findById(int id);

    /**
     * Выполняет сохранение сеанса. При успешном сохранении возвращает Optional с
     * объектом сеанса, у которого проинициализировано id. Иначе возвращает Optional.empty()
     *
     * @param session сохраняемый сеанс
     * @return Optional.of(session) при успешном сохранении, иначе Optional.empty()
     */
    Optional<Session> save(Session session);

    /**
     * Выполняет обновление объекта сеанс.
     *
     * @param session объект сеанс
     */
    void update(Session session);

    /**
     * Выполняет удаление сеанса по идентификатору. При успешном
     * удалении возвращает true, при неудачном false.
     *
     * @param id идентификатор сеанса
     * @return {@code true} при успешном удалении сеанса, иначе {@code false}
     */
    boolean deleteById(int id);
}
