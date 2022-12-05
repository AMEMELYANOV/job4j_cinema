package ru.job4j.cinema.repository;

import lombok.extern.slf4j.Slf4j;
import ru.job4j.cinema.model.Session;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация хранилища сеансов
 * @see ru.job4j.cinema.model.Session
 * @author Alexander Emelyanov
 * @version 1.0
 */
@Slf4j
public class PostgresSessionRepository implements SessionRepository {

    /**
     * SQL запрос по выбору всех сеансов из таблицы sessions
     */
    private static final String FIND_ALL_SELECT = """
            SELECT
                id,
                name,
                description
            FROM sessions
            """;

    /**
     * SQL запрос по выбору всех сеансов из таблицы sessions с фильтром по id
     */
    private static final String FIND_BY_ID_SELECT = FIND_ALL_SELECT + """
            WHERE id = ?
            """;

    /**
     * SQL запрос по добавлению строк в таблицу sessions
     */
    private static final String INSERT_INTO = """
            INSERT INTO sessions(name, description) VALUES (?, ?)
            """;

    /**
     * SQL запрос по обновлению данных сеанса в таблице sessions
     */
    private static final String UPDATE = """
            UPDATE sessions SET name = ?, description = ?
            WHERE id = ?
            """;

    /**
     * SQL запрос по удалению сеансов из таблицы sessions с фильтром по id
     */
    private static final String DELETE = """
            DELETE FROM sessions WHERE id = ?
            """;

    /**
     * Объект для выполнения подключения к базе данных приложения
     */
    private final DataSource pool;

    /**
     * Конструктор класса.
     *
     * @param dataSource объект для выполнения подключения к базе данных приложения
     */
    public PostgresSessionRepository(DataSource dataSource) {
        this.pool = dataSource;
    }

    /**
     * Возвращает список всех сеансов
     *
     * @return список всех сеансов
     */
    @Override
    public List<Session> findAll() {
        List<Session> sessions = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(FIND_ALL_SELECT)
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    sessions.add(getUserFromResultSet(it));
                }
            }
        } catch (Exception e) {
            log.info("Исключение в методе findAll() класса PostgresSessionRepository ", e);
        }
        return sessions;
    }

    /**
     * Выполняет поиск сеанса по идентификатору. При успешном нахождении возвращает
     * Optional с объектом сеанса. Иначе возвращает Optional.empty().
     *
     * @param id идентификатор сеанса
     * @return Optional.of(session) при успешном нахождении, иначе Optional.empty()
     */
    @Override
    public Optional<Session> findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(FIND_BY_ID_SELECT)
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return Optional.of(getUserFromResultSet(it));
                }
            }
        } catch (Exception e) {
            log.info("Исключение в методе findById() класса PostgresSessionRepository ", e);
        }
        return Optional.empty();
    }

    /**
     * Выполняет сохранение сеанса. При успешном сохранении возвращает Optional с
     * объектом сеанса, у которого проинициализировано id. Иначе возвращает Optional.empty()
     *
     * @param session сохраняемый сеанс
     * @return Optional.of(session) при успешном сохранении, иначе Optional.empty()
     */
    @Override
    public Optional<Session> save(Session session) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(INSERT_INTO,
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, session.getName());
            ps.setString(2, session.getDescription());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    session.setId(id.getInt(1));
                    return Optional.of(session);
                }
            }
        } catch (Exception e) {
            log.info("Исключение в методе save() класса PostgresSessionRepository ", e);
        }
        return Optional.empty();
    }

    /**
     * Выполняет обновление объекта сеанс.
     *
     * @param session объект сеанс
     */
    @Override
    public void update(Session session) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(UPDATE)
        ) {
            ps.setString(1, session.getName());
            ps.setString(2, session.getDescription());
            ps.setInt(3, session.getId());
            ps.execute();
        } catch (Exception e) {
            log.info("Исключение в методе update() класса PostgresSessionRepository ", e);
        }

    }

    /**
     * Выполняет удаление сеанса по идентификатору. При успешном
     * удалении возвращает true, при неудачном false.
     *
     * @param id идентификатор сеанса
     * @return {@code true} при успешном удалении сеанса, иначе {@code false}
     */
    @Override
    public boolean deleteById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(DELETE)
        ) {
            ps.setInt(1, id);
            int ids = ps.executeUpdate();
            if (ids > 0) {
                return true;
            }
        } catch (Exception e) {
            log.info("Исключение в методе deleteById() класса PostgresSessionRepository ", e);
        }
        return false;
    }

    /**
     * Вспомогательный метод выполняет создание
     * объекта Session из объекта ResultSet.
     *
     * @param it ResultSet SQL запроса к базе данных
     * @return объект Session
     */
    private static Session getUserFromResultSet(ResultSet it) throws SQLException {
        return new Session(it.getInt("id"), it.getString("name"),
                it.getString("description"));
    }
}
