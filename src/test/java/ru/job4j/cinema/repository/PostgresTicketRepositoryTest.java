package ru.job4j.cinema.repository;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.config.DataSourceConfig;
import ru.job4j.cinema.model.Show;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PostgresTicketRepositoryTest {

    /**
     * SQL запрос по очистке от данных таблицы users
     */
    private static final String CLEAR_TABLE_USERS = """
            DELETE FROM users;
            """;

    /**
     * SQL запрос по очистке от данных таблицы shows
     */
    private static final String CLEAR_TABLE_SHOWS = """
            DELETE FROM shows;
            """;

    /**
     * SQL запрос по очистке от данных таблицы tickets
     */
    private static final String CLEAR_TABLE_TICKETS = """
            DELETE FROM tickets;
            """;

    /**
     * Очистка таблиц: shows, users, tickets, выполняется после каждого теста.
     *
     * @throws SQLException если происходит ошибка доступа к базе данных
     */
    @AfterEach
    public void wipeTable() throws SQLException {
        try (BasicDataSource pool = new DataSourceConfig().loadPool();
             Connection connection = pool.getConnection();
             PreparedStatement statement1 = connection.prepareStatement(CLEAR_TABLE_TICKETS);
             PreparedStatement statement2 = connection.prepareStatement(CLEAR_TABLE_SHOWS);
             PreparedStatement statement3 = connection.prepareStatement(CLEAR_TABLE_USERS)
        ) {
            statement1.execute();
            statement2.execute();
            statement3.execute();
        }
    }

    /**
     * Создается объект ticket и сохраняется в базе данных.
     * По полю id объект ticket находится в базе данных, сохраняется в объект showFromDB
     * при помощи метода {@link PostgresTicketRepository#findById(int)}
     * и проверяется его эквивалентность объекту ticket по полю name.
     */
    @Test
    void whenSaveTicketThenGetTheSameFromDatabase() {
        PostgresTicketRepository ticketRepository = new PostgresTicketRepository(
                new DataSourceConfig().loadPool());
        PostgresShowRepository showRepository = new PostgresShowRepository(
                new DataSourceConfig().loadPool());
        PostgresUserRepository userRepository = new PostgresUserRepository(
                new DataSourceConfig().loadPool());
        User user = User.builder()
                .username("username")
                .email("email")
                .phone("phone")
                .password("pass")
                .build();
        Show show = Show.builder()
                .name("name")
                .description("description")
                .build();
        showRepository.save(show);
        userRepository.save(user);

        Ticket ticket = Ticket.builder()
                .show(show)
                .posRow(1)
                .cell(1)
                .user(user)
                .build();
        ticketRepository.save(ticket);
        Ticket ticketFromDB = ticketRepository.findById(ticket.getId()).get();

        assertThat(ticketRepository.findById(ticket.getId()).get()).isNotNull()
                .satisfies(oldTicket -> {
                    assertThat(oldTicket.getShow().getId())
                            .isEqualTo(ticketFromDB.getShow().getId());
                    assertThat(oldTicket.getPosRow()).isEqualTo(ticketFromDB.getPosRow());
                    assertThat(oldTicket.getCell()).isEqualTo(ticketFromDB.getCell());
                });
    }

    /**
     * Создается объект ticket и сохраняется в базе данных.
     * Выполняется изменение данных с обновлением объекта ticket в
     * базе данных при помощи метода {@link PostgresTicketRepository#update(Ticket)}.
     * По полю id объект ticket находится в базе данных, сохраняется в объект ticket
     * при помощи метода {@link PostgresTicketRepository#findById(int)}
     * и проверяется его эквивалентность объекту ticket по полю name.
     */
    @Test
    public void whenUpdateTicketThenGetTheSameFromDatabase() {
        PostgresTicketRepository ticketRepository = new PostgresTicketRepository(
                new DataSourceConfig().loadPool());
        PostgresShowRepository showRepository = new PostgresShowRepository(
                new DataSourceConfig().loadPool());
        PostgresUserRepository userRepository = new PostgresUserRepository(
                new DataSourceConfig().loadPool());
        User user = User.builder()
                .username("username")
                .email("email")
                .phone("phone")
                .password("pass")
                .build();
        Show show = Show.builder()
                .name("name")
                .description("description")
                .posterName("poster.jpg")
                .build();
        showRepository.save(show);
        userRepository.save(user);

        Ticket ticket = Ticket.builder()
                .show(show)
                .posRow(1)
                .cell(1)
                .user(user)
                .build();
        ticketRepository.save(ticket);
        ticket.setPosRow(2);
        ticketRepository.update(ticket);
        Ticket ticketFromDB = ticketRepository.findById(ticket.getId()).get();

        assertThat(ticket.getPosRow()).isEqualTo(ticketFromDB.getPosRow());
    }

    /**
     * Создаются объекты ticket1, ticket2 и сохраняются в базе данных.
     * По полю id объект ticket1 находится в базе данных при помощи метода
     * {@link PostgresTicketRepository#findById(int)} и проверяется на эквивалентность
     * объекту ticket1 по полю name.
     */
    @Test
    public void whenFindTicketByIdThenGetTicketFromDatabase() {
        PostgresTicketRepository ticketRepository = new PostgresTicketRepository(
                new DataSourceConfig().loadPool());
        PostgresShowRepository showRepository = new PostgresShowRepository(
                new DataSourceConfig().loadPool());
        PostgresUserRepository userRepository = new PostgresUserRepository(
                new DataSourceConfig().loadPool());
        User user1 = User.builder()
                .username("username1")
                .email("email1")
                .phone("phone1")
                .password("pass1")
                .build();
        User user2 = User.builder()
                .username("username2")
                .email("email2")
                .phone("phone2")
                .password("pass2")
                .build();
        Show show1 = Show.builder()
                .name("name1")
                .description("description1")
                .posterName("poster1.jpg")
                .build();
        Show show2 = Show.builder()
                .name("name2")
                .description("description2")
                .posterName("poster2.jpg")
                .build();
        showRepository.save(show1);
        showRepository.save(show2);
        userRepository.save(user1);
        userRepository.save(user2);

        Ticket ticket1 = Ticket.builder()
                .show(show1)
                .posRow(1)
                .cell(1)
                .user(user1)
                .build();
        Ticket ticket2 = Ticket.builder()
                .show(show2)
                .posRow(2)
                .cell(2)
                .user(user2)
                .build();
        ticketRepository.save(ticket1);
        ticketRepository.save(ticket2);

        assertThat(ticketRepository.findById(ticket1.getId()).get()).isNotNull()
                .satisfies(ticket -> {
                    assertThat(ticket.getShow().getId())
                            .isEqualTo(ticket1.getShow().getId());
                    assertThat(ticket.getPosRow()).isEqualTo(ticket1.getPosRow());
                    assertThat(ticket.getCell()).isEqualTo(ticket1.getCell());
                });
    }

    /**
     * Создается объект ticket и сохраняется в базе данных.
     * По полю id объект ticket находится в базе данных при помощи
     * метода {@link PostgresTicketRepository#findById(int)} и
     * проверяется на эквивалентность Optional.empty().
     */
    @Test
    public void whenFindTicketByIdThenDoNotGetTicketFromDatabase() {
        PostgresTicketRepository ticketRepository = new PostgresTicketRepository(
                new DataSourceConfig().loadPool());
        PostgresShowRepository showRepository = new PostgresShowRepository(
                new DataSourceConfig().loadPool());
        PostgresUserRepository userRepository = new PostgresUserRepository(
                new DataSourceConfig().loadPool());
        User user = User.builder()
                .username("username")
                .email("email")
                .phone("phone")
                .password("pass")
                .build();
        Show show = Show.builder()
                .name("name")
                .description("description")
                .build();
        showRepository.save(show);
        userRepository.save(user);

        Ticket ticket = Ticket.builder()
                .show(show)
                .posRow(1)
                .cell(1)
                .user(user)
                .build();
        ticketRepository.save(ticket);

        assertThat(ticketRepository.findById(ticket.getId() + 1)).isEqualTo(Optional.empty());
    }

    /**
     * Создается объект ticket и сохраняется в базе данных.
     * По полю id объект ticket удаляется из базы данных при помощи метода
     * {@link PostgresTicketRepository#deleteById(int)}
     * Метод {@link PostgresTicketRepository#deleteById(int)} при удалении
     * объекта возвращает true, вызов метода {@link PostgresTicketRepository#findById(int)}
     * проверяется на эквивалентность Optional.empty().
     */
    @Test
    public void whenDeleteTicketByIdThenDoNotGetTicketFromDatabase() {
        PostgresTicketRepository ticketRepository = new PostgresTicketRepository(
                new DataSourceConfig().loadPool());
        PostgresShowRepository showRepository = new PostgresShowRepository(
                new DataSourceConfig().loadPool());
        PostgresUserRepository userRepository = new PostgresUserRepository(
                new DataSourceConfig().loadPool());
        User user = User.builder()
                .username("username")
                .email("email")
                .phone("phone")
                .password("pass")
                .build();
        Show show = Show.builder()
                .name("name")
                .description("description")
                .build();
        showRepository.save(show);
        userRepository.save(user);

        Ticket ticket = Ticket.builder()
                .show(show)
                .posRow(1)
                .cell(1)
                .user(user)
                .build();
        ticketRepository.save(ticket);
        int id = ticket.getId();

        assertThat(ticketRepository.deleteById(id)).isEqualTo(true);
        assertThat(ticketRepository.findById(id)).isEqualTo(Optional.empty());
    }

    /**
     * Создаются объекты ticket1, ticket2 и сохраняются в базе данных.
     * Через вызов метода {@link PostgresTicketRepository#findAll()}
     * получаем список объекты tickets, который сортируется по id.
     * Выполняем проверку размера списка и содержание элементов
     * на эквивалентность объектам ticket1 и ticket2 по полям name.
     */
    @Test
    public void whenFindAllTicketsThenGetListOfAllTickets() {
        PostgresTicketRepository ticketRepository = new PostgresTicketRepository(
                new DataSourceConfig().loadPool());
        PostgresShowRepository showRepository = new PostgresShowRepository(
                new DataSourceConfig().loadPool());
        PostgresUserRepository userRepository = new PostgresUserRepository(
                new DataSourceConfig().loadPool());
        User user1 = User.builder()
                .username("username1")
                .email("email1")
                .phone("phone1")
                .password("pass1")
                .build();
        User user2 = User.builder()
                .username("username2")
                .email("email2")
                .phone("phone2")
                .password("pass2")
                .build();
        Show show1 = Show.builder()
                .name("name1")
                .description("description1")
                .build();
        Show show2 = Show.builder()
                .name("name2")
                .description("description2")
                .build();
        showRepository.save(show1);
        showRepository.save(show2);
        userRepository.save(user1);
        userRepository.save(user2);

        Ticket ticket1 = Ticket.builder()
                .show(show1)
                .posRow(1)
                .cell(1)
                .user(user1)
                .build();
        Ticket ticket2 = Ticket.builder()
                .show(show2)
                .posRow(2)
                .cell(2)
                .user(user2)
                .build();
        ticketRepository.save(ticket1);
        ticketRepository.save(ticket2);
        List<Ticket> tickets = ticketRepository.findAll();
        tickets.sort(Comparator.comparing(Ticket::getId));

        assertThat(tickets.size()).isEqualTo(2);
        assertThat(tickets.get(0)).isEqualTo(ticket1);
        assertThat(tickets.get(1)).isEqualTo(ticket2);
    }
}