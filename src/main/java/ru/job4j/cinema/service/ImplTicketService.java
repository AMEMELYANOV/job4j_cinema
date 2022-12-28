package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Ticket;

import java.util.List;
import java.util.Optional;

@Service
public class ImplTicketService implements TicketService{
    @Override
    public List<Ticket> findAll() {
        return null;
    }

    @Override
    public Optional<Ticket> findById(int id) {
        return Optional.empty();
    }

    @Override
    public Optional<Ticket> save(Ticket ticket) {
        return Optional.empty();
    }

    @Override
    public void update(Ticket ticket) {

    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }
}
