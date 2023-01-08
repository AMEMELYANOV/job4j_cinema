package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.TicketRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ImplTicketService implements TicketService {
    private final TicketRepository ticketRepository;

    public ImplTicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public List<Ticket> findAll() {
        return null;
    }

    @Override
    public Ticket findById(int id) {
        return null;
    }

    @Override
    public Ticket save(Ticket ticket) {
        Optional<Ticket> optionalTicket = ticketRepository.save(ticket);
        return optionalTicket.orElseThrow(() -> new NoSuchElementException("Билет уже продан"));
    }

    @Override
    public void update(Ticket ticket) {

    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }
}
