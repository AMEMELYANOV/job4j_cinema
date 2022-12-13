package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.repository.SessionRepository;

import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса по работе с сеансами
 * @see ru.job4j.cinema.model.Session
 * @author Alexander Emelyanov
 * @version 1.0
 */
@Service
public class ImplSessionService implements SessionService{

    private final SessionRepository sessionRepository;

    /**
     * Конструктор класса.
     *
     * @param sessionRepository объект для доступа к методам слоя repository
     */
    public ImplSessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    /**
     * Выполняет поиск сеанса по идентификатору. При успешном нахождении возвращает
     * Optional с объектом сеанса. Иначе возвращает Optional.empty().
     *
     * @param sessionId идентификатор сеанса
     * @return Optional.of(session) при успешном нахождении, иначе Optional.empty()
     */
    public Optional<Session> findById(Integer sessionId) {
        return sessionRepository.findById(sessionId);
    }

    @Override
    public List<Session> findAll() {
       return sessionRepository.findAll();
    }

    @Override
    public Optional<Session> findById(int id) {
        return Optional.empty();
    }

    @Override
    public Optional<Session> save(Session session) {
        return Optional.empty();
    }

    @Override
    public void update(Session session) {

    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }
}
