package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Реализация сервиса по работе с пользователями
 * @see ru.job4j.cinema.model.User
 * @author Alexander Emelyanov
 * @version 1.0
 */
@Service
public class ImplUserService implements UserService {

    /**
     * Объект для доступа к методам слоя UserRepository
     */
    private final UserRepository userRepository;

    /**
     * Конструктор класса.
     *
     * @param userRepository объект для доступа к методам слоя UserRepository
     */
    public ImplUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Возвращает список всех пользователей
     *
     * @return список всех пользователей
     */
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Выполняет поиск пользователя по идентификатору. При успешном нахождении возвращает
     * пользователя, иначе выбрасывает исключение.
     *
     * @param id идентификатор пользователя
     * @return пользователя при успешном нахождении
     * @exception NoSuchElementException, если user не найден
     */
    @Override
    public User findById(int id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(
                        String.format("Пользователь c id = %d не найден", id)));
    }

    /**
     * Выполняет сохранение пользователя. При успешном сохранении возвращает
     * пользователя, иначе выбрасывается исключение.
     *
     * @param user сохраняемый пользователь
     * @return пользователя при успешном нахождении
     * @exception IllegalArgumentException, если сохранение пользователя не произошло
     */
    @Override
    public User save(User user) {
        return userRepository.save(user).orElseThrow(
                () -> new IllegalArgumentException("Пользователь не сохранен"));
    }

    /**
     * Выполняет обновление пользователя.
     *
     * @param user обновляемый пользователь
     */
    @Override
    public void update(User user) {
        userRepository.update(user);
    }

    /**
     * Выполняет удаление пользователя по идентификатору. При успешном удалении
     * пользователя возвращает true, иначе выбрасывается исключение.
     *
     * @param id идентификатор пользователя
     * @return true при успешном удалении
     * @exception NoSuchElementException, если user не найден
     */
    @Override
    public boolean deleteById(int id) {
        boolean result = userRepository.deleteById(id);
        if (!result) {
            throw new NoSuchElementException(String.format("Пользователь c id = %d не найден", id));
        }
        return true;
    }

    /**
     * Выполняет поиск пользователя по почтовому адресу. При успешном нахождении возвращает
     * пользователя, иначе выбрасывает исключение.
     *
     * @param email почтовый адрес пользователя
     * @return пользователя при успешном нахождении
     * @exception NoSuchElementException, если пользователь не найден
     */
    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(
                () -> new NoSuchElementException(
                        String.format("Пользователь с email = %s не найден", email)));
    }

    /**
     * Выполняет сверку данных пользователя с входной формы с данными пользователя в базе по
     * почтовому адресу и паролю. При успешной проверке возвращает пользователя извлеченного
     * из базы данных, иначе выбрасывает исключение.
     * Для нахождения пользователя в базе данных используется метод
     * {@link ImplUserService#findUserByEmail(String)}.
     *
     * @param user пользователя
     * @return пользователя при успешном при совпадении пароля и почтового адреса
     * @exception IllegalArgumentException, если пароли пользователя не совпали
     */
    @Override
    public User validateUserLogin(User user) {
        User userFromDB = findUserByEmail(user.getEmail());
        if(!userFromDB.getPassword().equals(user.getPassword())){
            throw new IllegalArgumentException("Неправильно введен пароль");
        }
        return userFromDB;
    }

    /**
     * Выполняет сверку данных пользователя с формы регистрации с данными пользователя в базе по
     * почтовому адресу. Исключения выбрасываются при несовпадении паролей указанных при регистрации
     * и при наличии в базе пользователя с таким же паролем и номером телефона.
     * При успешной регистрации возвращает пользователя, созданного с данными формы регистрации
     *
     * @param user пользователя
     * @return пользователя при успешном при совпадении пароля и почтового адреса
     * @exception IllegalStateException, если пароли пользователя не совпали
     * @exception IllegalArgumentException, если пользователь с таким же email или паролем сохранен в базе
     */
    @Override
    public User validateUserReg(User user, String repassword) {
        if (!user.getPassword().equals(repassword)) {
            throw new IllegalStateException("Пароли не совпадают");
        }
        User userFromDB = userRepository.findUserByEmail(user.getEmail()).orElse(null);
        if (userFromDB != null) {
            throw new IllegalArgumentException(
                    String.format("Аккаунт с email = %s уже существует!", user.getEmail()));
        }
        return user;
    }
}
