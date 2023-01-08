package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Show;
import ru.job4j.cinema.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Сервис пользователей, логика работы с пользователями
 * @see User
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
     * Выполняет сохранение пользователя. При успешном сохранении возвращает Optional с
     * объектом пользователя, у которого проинициализировано id. Иначе возвращает Optional.empty()
     *
     * @param user сохраняемый пользователь
     * @return Optional.of(user) при успешном сохранении, иначе Optional.empty()
     */
    User save(User user);

    /**
     * Выполняет обновление объекта пользователь.
     *
     * @param user объект пользователя
     */
    void update(User user);

    /**
     * Выполняет удаление пользователя по идентификатору. При успешном
     * удалении возвращает true, при неудачном false.
     *
     * @param id идентификатор пользователя
     * @return {@code true} при успешном удалении пользователя, иначе {@code false}
     */
    boolean deleteById(int id);

    User findUserByEmail(String email);
}
