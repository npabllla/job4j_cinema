package ru.job4j.cinema.store;

import ru.job4j.cinema.model.Account;
import ru.job4j.cinema.model.Ticket;

import java.util.Collection;

public interface Store {
    Collection<Ticket> findAllTicket();

    Collection<Account> findAllAccount();

    Ticket findTicketById(int id);

    Account findAccountByEmail(String email);

    void save(Ticket ticket);

    void save(Account account);

    void buyTicket(Account account, int ticketId);
}
