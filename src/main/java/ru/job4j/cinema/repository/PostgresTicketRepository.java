package ru.job4j.cinema.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class PostgresTicketRepository implements TicketRepository {

    /**
     * SQL запрос по выбору всех билетов из таблицы tickets, при выполнении
     * запроса выполняется внутреннее соединение с таблицами sessions и users
     */
    private static final String FIND_ALL_SELECT = """ 
            SELECT
                t.id,
                t.session_id,
                t.pos_row,
                t.cell,
                t.user_id,
                s.name,
                s.description,
                u.username,
                u.email,
                u.phone,
                u.password
            FROM tickets t
            JOIN sessions s
                ON t.session_id = s.id
            JOIN users u
                ON t.user_id = u.id
            """;

    /**
     * SQL запрос по выбору всех билетов из таблицы tickets с фильтром по id
     */
    private static final String FIND_BY_ID_SELECT = FIND_ALL_SELECT + """ 
            WHERE t.id = ?
            """;

    /**
     * SQL запрос по добавлению строк в таблицу tickets
     */
    private static final String INSERT_INTO = """
            INSERT INTO tickets(session_id, pos_row, cell,
            user_id) VALUES (?, ?, ?, ?)
            """;

    /**
     * SQL запрос по обновлению данных билета в таблице tickets
     */
    private static final String UPDATE = """
            UPDATE tickets SET session_id = ?, pos_row = ?, cell = ?,
            user_id = ? WHERE id = ?
            """;

    /**
     * SQL запрос по удалению билетов из таблицы tickets с фильтром по id
     */
    private static final String DELETE = """
    DELETE FROM tickets WHERE id = ?
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
    public PostgresTicketRepository(DataSource dataSource) {
        this.pool = dataSource;
    }

    /**
     * Возвращает список всех билетов
     *
     * @return список всех билетов
     */
    @Override
    public List<Ticket> findAll() {
        List<Ticket> tickets = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(FIND_ALL_SELECT)
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    tickets.add(getTicketFromResultSet(it));
                }
            }
        } catch (Exception e) {
            log.info("Исключение в методе findAll() класса PostgresTicketRepository ", e);
        }
        return tickets;
    }

    /**
     * Выполняет поиск билета по идентификатору. При успешном нахождении возвращает
     * Optional с объектом билета. Иначе возвращает Optional.empty().
     *
     * @param id идентификатор билета
     * @return Optional.of(ticket) при успешном нахождении, иначе Optional.empty()
     */
    @Override
    public Optional<Ticket> findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(FIND_BY_ID_SELECT)
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return Optional.of(getTicketFromResultSet(it));
                }
            }
        } catch (Exception e) {
            log.info("Исключение в методе findById() класса PostgresTicketRepository ", e);
        }
        return Optional.empty();
    }

    /**
     * Выполняет сохранение билета. При успешном сохранении возвращает Optional с
     * объектом билета, у которого проинициализировано id. Иначе возвращает Optional.empty()
     * Сохранение не произойдет, если уникальный набор из session, pos_row и cell использовались
     * при сохранении в другом билете.
     *
     * @param ticket сохраняемый билет
     * @return Optional.of(ticket) при успешном сохранении, иначе Optional.empty()
     */
    @Override
    public Optional<Ticket> save(Ticket ticket) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(INSERT_INTO,
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, ticket.getSession().getId());
            ps.setInt(2, ticket.getPosRow());
            ps.setInt(3, ticket.getCell());
            ps.setInt(4, ticket.getUser().getId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    ticket.setId(id.getInt(1));
                    return Optional.of(ticket);
                }
            }
        } catch (Exception e) {
            log.info("Исключение в методе save() класса PostgresTicketRepository ", e);
        }
        return Optional.empty();
    }

    /**
     * Выполняет обновление билета.
     *
     * @param ticket объект билета
     */
    @Override
    public void update(Ticket ticket) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(UPDATE)
        ) {
            ps.setInt(1, ticket.getSession().getId());
            ps.setInt(2, ticket.getPosRow());
            ps.setInt(3, ticket.getCell());
            ps.setInt(4, ticket.getUser().getId());
            ps.setInt(5, ticket.getId());
            ps.execute();
        } catch (Exception e) {
            log.info("Исключение в методе update() класса PostgresTicketRepository ", e);
        }

    }

    /**
     * Выполняет удаление билета по идентификатору. При успешном
     * удалении возвращает true, при неудачном false.
     *
     * @param id идентификатор билета
     * @return {@code true} при успешном удалении билета, иначе {@code false}
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
            log.info("Исключение в методе deleteById() класса PostgresTicketRepository ", e);
        }
        return false;
    }

    /**
     * Вспомогательный метод выполняет создание
     * объекта Ticket из объекта ResultSet.
     *
     * @param it ResultSet SQL запроса к базе данных
     * @return объект Ticket
     */
    private static Ticket getTicketFromResultSet(ResultSet it) throws SQLException {
        return new Ticket(it.getInt("id"),
                new Session(it.getInt("session_id"),
                        it.getString("name"),
                        it.getString("description"),
                        it.getString("posterName")
                ),
                it.getInt("pos_row"),
                it.getInt("cell"),
                new User(it.getInt("user_id"),
                        it.getString("username"),
                        it.getString("email"),
                        it.getString("phone"),
                        it.getString("password")
                ));
    }
}
