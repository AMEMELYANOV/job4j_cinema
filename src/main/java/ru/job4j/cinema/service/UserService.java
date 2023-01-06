package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Show;
import ru.job4j.cinema.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Сервис пользователей, логика работы с пользователями
 * @see Show
 * @author Alexander Emelyanov
 * @version 1.0
 */
public interface UserService {

    /**
     * Возвращает список всех пользователей
     *
     * @return список всех пользователей
     */
    List<User> findAll();

    /**
     * Выполняет поиск пользователя по идентификатору. При успешном нахождении возвращает
     * Optional с объектом пользователя. Иначе возвращает Optional.empty().
     *
     * @param id идентификатор пользователя
     * @return Optional.of(user) при успешном нахождении, иначе Optional.empty()
     */
    User findById(int id);

    /**
     * Выполняет сохранение сеанса. При успешном сохранении возвращает Optional с
     * объектом сеанса, у которого проинициализировано id. Иначе возвращает Optional.empty()
     *
     * @param user сохраняемый сеанс
     * @return Optional.of(user) при успешном сохранении, иначе Optional.empty()
     */
    User save(User user);

    /**
     * Выполняет обновление объекта сеанс.
     *
     * @param user объект сеанс
     */
    void update(User user);

    /**
     * Выполняет удаление сеанса по идентификатору. При успешном
     * удалении возвращает true, при неудачном false.
     *
     * @param id идентификатор сеанса
     * @return {@code true} при успешном удалении сеанса, иначе {@code false}
     */
    boolean deleteById(int id);
}
