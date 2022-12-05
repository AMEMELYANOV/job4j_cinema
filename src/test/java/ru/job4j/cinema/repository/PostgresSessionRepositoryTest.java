package ru.job4j.cinema.repository;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.config.DataSourceConfig;
import ru.job4j.cinema.model.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тест класс реализации хранилища сеансов
 * @see ru.job4j.cinema.repository.PostgresSessionRepository
 * @author Alexander Emelyanov
 * @version 1.0
 */
class PostgresSessionRepositoryTest {

    /**
     * SQL запрос по очистке от данных таблицы sessions
     */
    private static final String CLEAR_TABLE = """
            DELETE FROM sessions
            """;

    /**
     * Очистка таблицы sessions, выполняется после каждого теста.
     *
     * @throws SQLException
     */
    @AfterEach
    public void wipeTable() throws SQLException {
        try (BasicDataSource pool = new DataSourceConfig().loadPool();
             Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(CLEAR_TABLE)
        ) {
            statement.execute();
        }
    }

    /**
     * Создается объект session и сохраняется в базе данных.
     * По полю id объект session находится в базе данных, сохраняется в объект sessionFromDB
     * при помощи метода {@link PostgresSessionRepository#findById(int)}
     * и проверяется его эквивалентность объекту session по полю name.
     */
    @Test
    void whenSaveSessionThenGetTheSameFromDatabase() {
        PostgresSessionRepository repository = new PostgresSessionRepository(
                new DataSourceConfig().loadPool());
        Session session = Session.builder()
                .name("Name")
                .description("Description")
                .build();

        repository.save(session);
        Session sessionFromDB = repository.findById(session.getId()).get();

        assertThat(session.getName()).isEqualTo(sessionFromDB.getName());
    }

    /**
     * Создается объект session и сохраняется в базе данных.
     * Выполняется изменение данных с обновлением объекта session в
     * базе данных при помощи метода {@link PostgresSessionRepository#update(Session)}.
     * По полю id объект session находится в базе данных, сохраняется в объект sessionFromDB
     * при помощи метода {@link PostgresSessionRepository#findById(int)}
     * и проверяется его эквивалентность объекту session по полю name.
     */
    @Test
    public void whenUpdateSessionThenGetTheSameFromDatabase() {
        PostgresSessionRepository repository = new PostgresSessionRepository(
                new DataSourceConfig().loadPool());
        Session session = Session.builder()
                .name("Name")
                .description("Description")
                .build();

        repository.save(session);
        session.setName("Name2");
        repository.update(session);

        Session sessionFromDb = repository.findById(session.getId()).get();
        assertThat(sessionFromDb.getName()).isEqualTo(session.getName());
    }

    /**
     * Создаются объекты session1, session2 и сохраняются в базе данных.
     * По полю id объект session1 находится в базе данных при помощи метода
     * {@link PostgresSessionRepository#findById(int)} и проверяется на эквивалентность
     * объекту session1 по полю name.
     */
    @Test
    public void whenFindSessionByIdThenGetSessionFromDatabase() {
        PostgresSessionRepository repository = new PostgresSessionRepository(
                new DataSourceConfig().loadPool());
        Session session1 = Session.builder()
                .name("Name1")
                .description("Description1")
                .build();
        Session session2 = Session.builder()
                .name("Name2")
                .description("Description2")
                .build();

        repository.save(session1);
        repository.save(session2);

        Session sessionFromDB = repository.findById(session1.getId()).get();
        assertThat(sessionFromDB.getName()).isEqualTo(session1.getName());
    }

    /**
     * Создается объект session и сохраняется в базе данных.
     * По полю id объект session находится в базе данных при помощи
     * метода {@link PostgresSessionRepository#findById(int)} и
     * проверяется на эквивалентность Optional.empty().
     */
    @Test
    public void whenFindSessionByIdThenDoNotGetSessionFromDatabase() {
        PostgresSessionRepository repository = new PostgresSessionRepository(
                new DataSourceConfig().loadPool());
        Session session = Session.builder()
                .name("Name")
                .description("Description")
                .build();

        repository.save(session);

        assertThat(repository.findById(session.getId() + 1)).isEqualTo(Optional.empty());
    }

    /**
     * Создается объект session и сохраняется в базе данных.
     * По полю id объект session удаляется из базы данных при помощи метода
     * {@link PostgresSessionRepository#deleteById(int)}
     * Метод {@link PostgresSessionRepository#deleteById(int)} при удалении
     * объекта возвращает true, вызов метода {@link PostgresSessionRepository#findById(int)}
     * проверяется на эквивалентность Optional.empty().
     */
    @Test
    public void whenDeleteSessionByIdIsTrueAndThenDoNotGetSessionFromDatabase() {
        PostgresSessionRepository repository = new PostgresSessionRepository(
                new DataSourceConfig().loadPool());
        Session session = Session.builder()
                .name("Name")
                .description("Description")
                .build();

        repository.save(session);
        int id = session.getId();

        assertThat(repository.deleteById(id)).isEqualTo(true);
        assertThat(repository.findById(id)).isEqualTo(Optional.empty());
    }

    /**
     * Создаются объекты session1, session2 и сохраняются в базе данных.
     * Через вызов метода {@link PostgresSessionRepository#findAll()}
     * получаем список объекты sessions, который сортируется по id.
     * Выполняем проверку размера списка и содержание элементов
     * на эквивалентность объектам session1 и session2 по полям name.
     */
    @Test
    public void whenFindAllSessionsThenGetListOfAllSessions() {
        PostgresSessionRepository repository = new PostgresSessionRepository(
                new DataSourceConfig().loadPool());
        Session session1 = Session.builder()
                .name("Name1")
                .description("Description1")
                .build();
        Session session2 = Session.builder()
                .name("Name2")
                .description("Description2")
                .build();

        repository.save(session1);
        repository.save(session2);
        List<Session> sessions = repository.findAll();
        sessions.sort(Comparator.comparing(Session::getId));

        assertThat(sessions.size()).isEqualTo(2);
        assertThat(sessions.get(0).getName()).isEqualTo(session1.getName());
        assertThat(sessions.get(1).getName()).isEqualTo(session2.getName());
    }
}