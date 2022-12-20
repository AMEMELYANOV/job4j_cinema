package ru.job4j.cinema.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация хранилища пользователей
 * @see ru.job4j.cinema.model.User
 * @author Alexander Emelyanov
 * @version 1.0
 */
@Slf4j
@Repository
public class PostgresUserRepository implements UserRepository {

    /**
     * SQL запрос по выбору всех пользователей из таблицы users
     */
    private static final String FIND_ALL_SELECT = """
            SELECT
                id,
                username,
                email,
                phone,
                password
            FROM users
            """;

    /**
     * SQL запрос по выбору всех пользователей из таблицы users с фильтром по id
     */
    private static final String FIND_BY_ID_SELECT = FIND_ALL_SELECT + """
            WHERE id = ?
            """;

    /**
     * SQL запрос по добавлению строк в таблицу users
     */
    private static final String INSERT_INTO = """
            INSERT INTO users(username, email, phone,
            password) VALUES (?, ?, ?, ?)
            """;

    /**
     * SQL запрос по обновлению данных пользователя в таблице users
     */
    private static final String UPDATE = """
            UPDATE users SET username = ?, email = ?, phone = ?,
            password = ? WHERE id = ?
            """;

    /**
     * SQL запрос по удалению пользователей из таблицы users с фильтром по id
     */
    private static final String DELETE = """
            DELETE FROM users WHERE id = ?
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
    public PostgresUserRepository(DataSource dataSource) {
        this.pool = dataSource;
    }

    /**
     * Возвращает список всех пользователей.
     *
     * @return список всех пользователей
     */
    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(FIND_ALL_SELECT)
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    users.add(getUserFromResultSet(it));
                }
            }
        } catch (Exception e) {
            log.info("Исключение в методе findAll() класса PostgresUserRepository ", e);
        }
        return users;
    }

    /**
     * Выполняет поиск пользователя по идентификатору. При успешном нахождении возвращает
     * Optional с объектом пользователя. Иначе возвращает Optional.empty().
     *
     * @param id идентификатор пользователя
     * @return Optional.of(user) при успешном нахождении, иначе Optional.empty()
     */
    @Override
    public Optional<User> findById(int id) {
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
            log.info("Исключение в методе findById() класса PostgresUserRepository ", e);
        }
        return Optional.empty();
    }

    /**
     * Выполняет сохранение пользователя. При успешном сохранении возвращает Optional с
     * объектом пользователя, у которого проинициализировано id. Иначе возвращает Optional.empty()
     * Сохранение не произойдет, если email или номер телефона использовались при регистрации
     * другим пользователем.
     *
     * @param user сохраняемый пользователь
     * @return Optional.of(user) при успешном сохранении, иначе Optional.empty()
     */
    @Override
    public Optional<User> save(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(INSERT_INTO,
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getPassword());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                    return Optional.of(user);
                }
            }
        } catch (Exception e) {
            log.info("Исключение в методе save() класса PostgresUserRepository ", e);
        }
        return Optional.empty();
    }

    /**
     * Выполняет обновление пользователя.
     *
     * @param user объект пользователя
     */
    @Override
    public void update(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(UPDATE)
        ) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getPassword());
            ps.setInt(5, user.getId());
            ps.execute();
        } catch (Exception e) {
            log.info("Исключение в методе update() класса PostgresUserRepository ", e);
        }
    }

    /**
     * Выполняет удаление пользователя по идентификатору. При успешном
     * удалении возвращает true, при неудачном false.
     *
     * @param id идентификатор пользователя
     * @return {@code true} при успешном удалении пользователя, иначе {@code false}
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
            log.info("Исключение в методе deleteById() класса PostgresUserRepository ", e);
        }
        return false;
    }

    /**
     * Вспомогательный метод выполняет создание
     * объекта User из объекта ResultSet.
     *
     * @param it ResultSet SQL запроса к базе данных
     * @return объект User
     */
    private static User getUserFromResultSet(ResultSet it) throws SQLException {
        return new User(it.getInt("id"), it.getString("username"),
                it.getString("email"), it.getString("phone"), it.getString("password"));
    }
}
