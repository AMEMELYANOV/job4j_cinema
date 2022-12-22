package ru.job4j.cinema.repository;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.config.DataSourceConfig;
import ru.job4j.cinema.model.Show;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тест класс реализации хранилища сеансов
 * @see PostgresShowRepository
 * @author Alexander Emelyanov
 * @version 1.0
 */
class PostgresShowRepositoryTest {

    /**
     * SQL запрос по очистке от данных таблицы shows
     */
    private static final String CLEAR_TABLE = """
            DELETE FROM shows
            """;

    /**
     * Очистка таблицы shows, выполняется после каждого теста.
     *
     * @throws SQLException если происходит ошибка доступа к базе данных
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
     * Создается объект show и сохраняется в базе данных.
     * По полю id объект show в базе данных, сохраняется в объект showFromDB
     * при помощи метода {@link PostgresShowRepository#findById(int)}
     * и проверяется его эквивалентность объекту show по полю name.
     */
    @Test
    void whenSaveShowThenGetTheSameFromDatabase() {
        PostgresShowRepository repository = new PostgresShowRepository(
                new DataSourceConfig().loadPool());
        Show show = Show.builder()
                .name("Name")
                .description("Description")
                .build();

        repository.save(show);
        Show showFromDB = repository.findById(show.getId()).get();

        assertThat(show.getName()).isEqualTo(showFromDB.getName());
    }

    /**
     * Создается объект show и сохраняется в базе данных.
     * Выполняется изменение данных с обновлением объекта show в
     * базе данных при помощи метода {@link PostgresShowRepository#update(Show)}.
     * По полю id объект show находится в базе данных, сохраняется в объект showFromDB
     * при помощи метода {@link PostgresShowRepository#findById(int)}
     * и проверяется его эквивалентность объекту show по полю name.
     */
    @Test
    public void whenUpdateShowThenGetTheSameFromDatabase() {
        PostgresShowRepository repository = new PostgresShowRepository(
                new DataSourceConfig().loadPool());
        Show show = Show.builder()
                .name("Name")
                .description("Description")
                .build();

        repository.save(show);
        show.setName("Name2");
        repository.update(show);

        Show showFromDb = repository.findById(show.getId()).get();
        assertThat(showFromDb.getName()).isEqualTo(show.getName());
    }

    /**
     * Создаются объекты show1, show2 и сохраняются в базе данных.
     * По полю id объект show1 находится в базе данных при помощи метода
     * {@link PostgresShowRepository#findById(int)} и проверяется на эквивалентность
     * объекту show1 по полю name.
     */
    @Test
    public void whenFindShowByIdThenGetShowFromDatabase() {
        PostgresShowRepository repository = new PostgresShowRepository(
                new DataSourceConfig().loadPool());
        Show show1 = Show.builder()
                .name("Name1")
                .description("Description1")
                .build();
        Show show2 = Show.builder()
                .name("Name2")
                .description("Description2")
                .build();

        repository.save(show1);
        repository.save(show2);

        Show showFromDB = repository.findById(show1.getId()).get();
        assertThat(showFromDB.getName()).isEqualTo(show1.getName());
    }

    /**
     * Создается объект show и сохраняется в базе данных.
     * По полю id объект show находится в базе данных при помощи
     * метода {@link PostgresShowRepository#findById(int)} и
     * проверяется на эквивалентность Optional.empty().
     */
    @Test
    public void whenFindShowByIdThenDoNotGetShowFromDatabase() {
        PostgresShowRepository repository = new PostgresShowRepository(
                new DataSourceConfig().loadPool());
        Show show = Show.builder()
                .name("Name")
                .description("Description")
                .build();

        repository.save(show);

        assertThat(repository.findById(show.getId() + 1)).isEqualTo(Optional.empty());
    }

    /**
     * Создается объект show и сохраняется в базе данных.
     * По полю id объект show удаляется из базы данных при помощи метода
     * {@link PostgresShowRepository#deleteById(int)}
     * Метод {@link PostgresShowRepository#deleteById(int)} при удалении
     * объекта возвращает true, вызов метода {@link PostgresShowRepository#findById(int)}
     * проверяется на эквивалентность Optional.empty().
     */
    @Test
    public void whenDeleteShowByIdIsTrueAndThenDoNotGetShowFromDatabase() {
        PostgresShowRepository repository = new PostgresShowRepository(
                new DataSourceConfig().loadPool());
        Show show = Show.builder()
                .name("Name")
                .description("Description")
                .build();

        repository.save(show);
        int id = show.getId();

        assertThat(repository.deleteById(id)).isEqualTo(true);
        assertThat(repository.findById(id)).isEqualTo(Optional.empty());
    }

    /**
     * Создаются объекты show1, show2 и сохраняются в базе данных.
     * Через вызов метода {@link PostgresShowRepository#findAll()}
     * получаем список объекты shows, который сортируется по id.
     * Выполняем проверку размера списка и содержание элементов
     * на эквивалентность объектам show1 и show2 по полям name.
     */
    @Test
    public void whenFindAllShowsThenGetListOfAllShows() {
        PostgresShowRepository repository = new PostgresShowRepository(
                new DataSourceConfig().loadPool());
        Show show1 = Show.builder()
                .name("Name1")
                .description("Description1")
                .build();
        Show show2 = Show.builder()
                .name("Name2")
                .description("Description2")
                .build();

        repository.save(show1);
        repository.save(show2);
        List<Show> shows = repository.findAll();
        shows.sort(Comparator.comparing(Show::getId));

        assertThat(shows.size()).isEqualTo(2);
        assertThat(shows.get(0).getName()).isEqualTo(show1.getName());
        assertThat(shows.get(1).getName()).isEqualTo(show2.getName());
    }
}