package ru.job4j.cinema.model;

import java.util.Objects;

public class Ticket {
    private int id;
    private int row;
    private int column;
    private int price;
    private int accountId;
    private String status;

    public Ticket(int id, int row, int column, int price, String status, int accountId) {
        this.id = id;
        this.row = row;
        this.column = column;
        this.price = price;
        this.accountId = accountId;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ticket ticket = (Ticket) o;
        return id == ticket.id && row == ticket.row && column == ticket.column && price == ticket.price && accountId == ticket.accountId && Objects.equals(status, ticket.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, row, column, price, accountId, status);
    }

    @Override
    public String toString() {
        return "Ticket{"
                + "id=" + id
                + ", row=" + row
                + ", column=" + column
                + ", price=" + price
                + ", accountId=" + accountId
                + ", status='" + status + '\''
                + '}';
    }
}
