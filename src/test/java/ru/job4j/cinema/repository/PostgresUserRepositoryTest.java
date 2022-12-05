package ru.job4j.cinema.repository;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.config.DataSourceConfig;
import ru.job4j.cinema.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PostgresUserRepositoryTest {

    /**
     * SQL запрос по очистке от данных таблицы users
     */
    private static final String CLEAR_TABLE = """
            DELETE FROM users
            """;

    /**
     * Очистка таблицы users, выполняется после каждого теста.
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
     * Создается объект user и сохраняется в базе данных.
     * По полю id объект user находится в базе данных, сохраняется в объект userFromDB
     * при помощи метода {@link PostgresUserRepository#findById(int)}
     * и проверяется его эквивалентность объекту user по полю name.
     */
    @Test
    void whenSaveUserThenGetTheSameFromDatabase() {
        PostgresUserRepository repository = new PostgresUserRepository(
                new DataSourceConfig().loadPool());
        User user = User.builder()
                .username("Name")
                .email("mail@mail.com")
                .phone("123")
                .password("password")
                .build();

        repository.save(user);
        User userFromDB = repository.findById(user.getId()).get();

        assertThat(user.getUsername()).isEqualTo(userFromDB.getUsername());
    }

    /**
     * Создается объект user и сохраняется в базе данных.
     * Выполняется изменение данных с обновлением объекта user в
     * базе данных при помощи метода {@link PostgresUserRepository#update(User)}.
     * По полю id объект user находится в базе данных, сохраняется в объект userFromDB
     * при помощи метода {@link PostgresUserRepository#findById(int)}
     * и проверяется его эквивалентность объекту user по полю name.
     */
    @Test
    public void whenUpdateUserThenGetTheSameFromDatabase() {
        PostgresUserRepository repository = new PostgresUserRepository(
                new DataSourceConfig().loadPool());
        User user = User.builder()
                .username("Name")
                .email("mail@mail.com")
                .phone("123")
                .password("password")
                .build();
        repository.save(user);
        user.setUsername("Name2");
        repository.update(user);

        User userFromDb = repository.findById(user.getId()).get();
        assertThat(userFromDb.getUsername()).isEqualTo(user.getUsername());
    }

    /**
     * Создаются объекты user1, user2 и сохраняются в базе данных.
     * По полю id объект user1 находится в базе данных при помощи метода
     * {@link PostgresUserRepository#findById(int)} и проверяется на эквивалентность
     * объекту user1 по полю name.
     */
    @Test
    public void whenFindUserByIdThenGetUserFromDatabase() {
        PostgresUserRepository repository = new PostgresUserRepository(
                new DataSourceConfig().loadPool());
        User user1 = User.builder()
                .username("Name1")
                .email("mail1@mail.com")
                .phone("1231")
                .password("password")
                .build();
        User user2 = User.builder()
                .username("Name2")
                .email("mail2@mail.com")
                .phone("1232")
                .password("password")
                .build();
        repository.save(user1);
        repository.save(user2);

        User userFromDB = repository.findById(user1.getId()).get();
        assertThat(userFromDB.getUsername()).isEqualTo(user1.getUsername());
    }

    /**
     * Создается объект user и сохраняется в базе данных.
     * По полю id объект user находится в базе данных при помощи
     * метода {@link PostgresUserRepository#findById(int)} и
     * проверяется на эквивалентность Optional.empty().
     */
    @Test
    public void whenFindUserByIdThenDoNotGetUserFromDatabase() {
        PostgresUserRepository repository = new PostgresUserRepository(
                new DataSourceConfig().loadPool());
        User user = User.builder()
                .username("Name")
                .email("mail@mail.com")
                .phone("123")
                .password("password")
                .build();
        repository.save(user);

        assertThat(repository.findById(user.getId() + 1)).isEqualTo(Optional.empty());
    }

    /**
     * Создается объект user и сохраняется в базе данных.
     * По полю id объект user удаляется из базы данных при помощи метода
     * {@link PostgresUserRepository#deleteById(int)}
     * Метод {@link PostgresUserRepository#deleteById(int)} при удалении
     * объекта возвращает true, вызов метода {@link PostgresUserRepository#findById(int)}
     * проверяется на эквивалентность Optional.empty().
     */
    @Test
    public void whenDeleteUserByIdIsTrueAndThenDoNotGetUserFromDatabase() {
        PostgresUserRepository repository = new PostgresUserRepository(
                new DataSourceConfig().loadPool());
        User user = User.builder()
                .username("Name")
                .email("mail@mail.com")
                .phone("123")
                .password("password")
                .build();
        repository.save(user);
        int id = user.getId();

        assertThat(repository.deleteById(id)).isEqualTo(true);
        assertThat(repository.findById(id)).isEqualTo(Optional.empty());
    }

    /**
     * Создаются объекты user1, user2 и сохраняются в базе данных.
     * Через вызов метода {@link PostgresUserRepository#findAll()}
     * получаем список объекты users, который сортируется по id.
     * Выполняем проверку размера списка и содержание элементов
     * на эквивалентность объектам user1 и user2 по полям name.
     */
    @Test
    public void whenFindAllUsersThenGetListOfAllUsers() {
        PostgresUserRepository repository = new PostgresUserRepository(
                new DataSourceConfig().loadPool());
        User user1 = User.builder()
                .username("Name1")
                .email("mail1@mail.com")
                .phone("1231")
                .password("password1")
                .build();
        User user2 = User.builder()
                .username("Name2")
                .email("mail2@mail.com")
                .phone("1232")
                .password("password2")
                .build();
        repository.save(user1);
        repository.save(user2);
        List<User> users = repository.findAll();
        users.sort(Comparator.comparing(User::getId));

        assertThat(users.size()).isEqualTo(2);
        assertThat(users.get(0)).isEqualTo(user1);
        assertThat(users.get(1)).isEqualTo(user2);
    }
}