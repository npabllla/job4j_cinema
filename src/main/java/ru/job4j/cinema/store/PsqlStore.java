package ru.job4j.cinema.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.cinema.model.Account;
import ru.job4j.cinema.model.Ticket;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

public class PsqlStore implements Store {

    private static final Logger LOG = LoggerFactory.getLogger(PsqlStore.class.getName());

    private final BasicDataSource pool = new BasicDataSource();

    private PsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("db.properties")
        )) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Ticket> findAllTicket() {
        Collection<Ticket> tickets = new ArrayList<>();
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement("select * from ticket order by id")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                tickets.add(new Ticket(
                        resultSet.getInt("id"),
                        resultSet.getInt("row"),
                        resultSet.getInt("col"),
                        resultSet.getInt("price"),
                        resultSet.getString("status"),
                        resultSet.getInt("account_id")));
            }
        } catch (SQLException e) {
            LOG.error("Exception", e);
        }
        return tickets;
    }

    @Override
    public Collection<Account> findAllAccount() {
        Collection<Account> accounts = new ArrayList<>();
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select * from account")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                accounts.add(new Account(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("email"),
                        resultSet.getString("phone")
                ));
            }
        } catch (SQLException e) {
            LOG.error("Exception", e);
        }
        return accounts;
    }

    @Override
    public Ticket findTicketById(int id) {
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select * from ticket where id =?")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Ticket(
                        resultSet.getInt("id"),
                        resultSet.getInt("row"),
                        resultSet.getInt("col"),
                        resultSet.getInt("price"),
                        resultSet.getString("status"),
                        resultSet.getInt("account_id"));
            }
        } catch (SQLException e) {
            LOG.error("Exception", e);
        }
        return null;
    }

    @Override
    public Account findAccountByEmail(String email) {
        Account account = null;
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select * from account where email =?")) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                account = new Account(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("email"),
                        resultSet.getString("phone")
                );
            }
        } catch (SQLException e) {
            LOG.error("Exception", e);
        }
        return account;
    }

    @Override
    public void save(Ticket ticket) {
        if (ticket.getId() == 0) {
            try (Connection connection = pool.getConnection();
                 PreparedStatement statement = connection.prepareStatement(
                         "insert  into ticket (row, col, price) values (?, ?, ?)",
                         PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, ticket.getRow());
                statement.setInt(2, ticket.getColumn());
                statement.setInt(3, ticket.getPrice());
                statement.executeUpdate();
            } catch (SQLException e) {
                LOG.error("Exception", e);
            }
        } else {
            try (Connection connection = pool.getConnection();
                 PreparedStatement statement = connection.prepareStatement(
                         "update ticket set account_id =? where id =?")) {
                statement.setInt(1, ticket.getAccountId());
                statement.setInt(2, ticket.getId());
                statement.executeUpdate();
            } catch (SQLException e) {
                LOG.error("Exception", e);
            }
        }
    }

    @Override
    public void save(Account account) {
        if (account.getId() == 0) {
            try (Connection connection = pool.getConnection();
                 PreparedStatement statement = connection.prepareStatement(
                         "insert into account (username, email, phone) values (?, ?, ?)",
                         PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, account.getName());
                statement.setString(2, account.getEmail());
                statement.setString(3, account.getPhone());
                statement.executeUpdate();
            } catch (SQLException e) {
                LOG.error("Exception", e);
            }
        } else {
            try (Connection connection = pool.getConnection();
                 PreparedStatement statement = connection.prepareStatement(
                         "update account set id =? where id =?")) {
                statement.setInt(1, account.getId());
                statement.setInt(2, account.getId());
                statement.executeUpdate();
            } catch (SQLException e) {
                LOG.error("Exception", e);
            }
        }
    }

    @Override
    public void buyTicket(Account account, int ticketId) {
        if (findAccountByEmail(account.getEmail()) == null) {
            save(account);
        }
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "update ticket set account_id=(select id from account where email=?) where id=?")) {
            statement.setString(1, account.getEmail());
            statement.setInt(2, ticketId);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Exception", e);
        }
    }
}
