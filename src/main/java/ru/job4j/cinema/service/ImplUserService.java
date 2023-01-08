package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Реализация сервиса по работе с пользователями
 *
 * @author Alexander Emelyanov
 * @version 1.0
 * @see ru.job4j.cinema.model.User
 */
@Service
public class ImplUserService implements UserService {

    private final UserRepository userRepository;

    /**
     * Конструктор класса.
     *
     * @param userRepository   объект для доступа к методам слоя UserRepository
     */
    public ImplUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Выполняет поиск сеанса по идентификатору. При успешном нахождении возвращает
     * Optional с объектом сеанса. Иначе возвращает Optional.empty().
     *
     * @param id идентификатор сеанса
     * @return Optional.of(show) при успешном нахождении, иначе Optional.empty()
     */
    @Override
    public User findById(int id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user).orElseThrow(
                () -> new NoSuchElementException("Пользователь не создан"));
    }

    @Override
    public void update(User user) {
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElse(null);
    }
}
