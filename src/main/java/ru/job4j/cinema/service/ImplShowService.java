package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Show;
import ru.job4j.cinema.repository.ShowRepository;

import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса по работе с сеансами
 * @see ru.job4j.cinema.model.Show
 * @author Alexander Emelyanov
 * @version 1.0
 */
@Service
public class ImplShowService implements ShowService {

    private final ShowRepository showRepository;

    /**
     * Конструктор класса.
     *
     * @param showRepository объект для доступа к методам слоя repository
     */
    public ImplShowService(ShowRepository showRepository) {
        this.showRepository = showRepository;
    }

    /**
     * Выполняет поиск сеанса по идентификатору. При успешном нахождении возвращает
     * Optional с объектом сеанса. Иначе возвращает Optional.empty().
     *
     * @param showId идентификатор сеанса
     * @return Optional.of(show) при успешном нахождении, иначе Optional.empty()
     */
    public Optional<Show> findById(Integer showId) {
        return showRepository.findById(showId);
    }

    @Override
    public List<Show> findAll() {
       return showRepository.findAll();
    }

    @Override
    public Optional<Show> findById(int id) {
        return Optional.empty();
    }

    @Override
    public Optional<Show> save(Show show) {
        return Optional.empty();
    }

    @Override
    public void update(Show show) {

    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }
}
