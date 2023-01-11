package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.TicketRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Реализация сервиса по работе с билета
 * @see ru.job4j.cinema.model.Ticket
 * @author Alexander Emelyanov
 * @version 1.0
 */
@Service
public class ImplTicketService implements TicketService {

    /**
     * Объект для доступа к методам TicketRepository
     */
    private final TicketRepository ticketRepository;

    /**
     * Конструктор класса.
     *
     * @param ticketRepository объект для доступа к методам TicketRepository
     * @see ru.job4j.cinema.repository.TicketRepository
     */
    public ImplTicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    /**
     * Возвращает список всех билетов
     *
     * @return {@code List<Ticket>} - список всех билетов
     */
    @Override
    public List<Ticket> findAll() {
        return null;
    }

    /**
     * Выполняет поиск билета по идентификатору. При успешном нахождении возвращает
     * билет, иначе выбрасывает исключение.
     *
     * @param id идентификатор билета
     * @return билет при успешном нахождении
     * @exception NoSuchElementException, если билет не найден
     */
    @Override
    public Ticket findById(int id) {
        return ticketRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(
                        String.format("Билет c id = %d не найден", id)));
    }

    /**
     * Выполняет сохранение билета. При успешном сохранении возвращает
     * сохраненный билет, иначе выбрасывается исключение.
     *
     * @param ticket сохраняемый билет
     * @return билет при успешном сохранении
     * @exception IllegalArgumentException, если сохранение билета не произошло
     */
    @Override
    public Ticket save(Ticket ticket) {
        Optional<Ticket> optionalTicket = ticketRepository.save(ticket);
        return optionalTicket.orElseThrow(() -> new IllegalArgumentException("Билет уже продан"));
    }

    /**
     * Выполняет обновление билета.
     *
     * @param ticket обновляемый билет
     */
    @Override
    public void update(Ticket ticket) {

    }

    /**
     * Выполняет удаление билета по идентификатору. При успешном удалении
     * билета возвращает true, иначе выбрасывается исключение.
     *
     * @param id идентификатор билета
     * @return true при успешном удалении
     * @exception NoSuchElementException, если билет не найден
     */
    @Override
    public boolean deleteById(int id) {
        if (!ticketRepository.deleteById(id)) {
            throw new NoSuchElementException(
                    String.format("Билет c id = %d не найден", id));
        }
        return true;
    }
}
