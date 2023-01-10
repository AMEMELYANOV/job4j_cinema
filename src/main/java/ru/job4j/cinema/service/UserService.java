package ru.job4j.cinema.service;

import ru.job4j.cinema.model.User;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Сервис пользователей, логика работы с пользователями
 * @author Alexander Emelyanov
 * @version 1.0
 * @see User
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
     * пользователя, иначе выбрасывает исключение.
     *
     * @param id идентификатор пользователя
     * @return пользователя при успешном нахождении
     * @exception NoSuchElementException, если user не найден.
     */
    User findById(int id);

    /**
     * Выполняет сохранение пользователя. При успешном сохранении возвращает
     * пользователя, иначе выбрасывается исключение.
     *
     * @param user сохраняемый пользователь
     * @return пользователя при успешном нахождении
     * @exception IllegalArgumentException, если сохранение пользователя не произошло.
     */
    User save(User user);

    /**
     * Выполняет обновление пользователя.
     *
     * @param user обновляемый пользователь
     */
    void update(User user);

    /**
     * Выполняет удаление пользователя по идентификатору. При успешном удалении
     * пользователя возвращает true, иначе выбрасывается исключение.
     *
     * @param id идентификатор пользователя
     * @return true при успешном удалении
     * @exception NoSuchElementException, если user не найден.
     */
    boolean deleteById(int id);

    /**
     * Выполняет поиск пользователя по почтовому адресу. При успешном нахождении возвращает
     * пользователя, иначе выбрасывает исключение.
     *
     * @param email почтовый адрес пользователя
     * @return пользователя при успешном нахождении
     * @exception NoSuchElementException, если пользователь не найден.
     */
    User findUserByEmail(String email);

    /**
     * Выполняет проверку пользователя в базе по почтовому адресу и паролю. При успешной
     * проверке возвращает true, иначе false.
     *
     * @param user пользователя
     * @return true при успешном при совпадении пароля и почтового адреса, иначе false
     */
    boolean validateUserLogin(User user);
}
