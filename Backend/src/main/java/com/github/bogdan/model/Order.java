package com.github.bogdan.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Objects;

@DatabaseTable(tableName = "order")
public class Order implements Filtration{
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    private User user;

    @DatabaseField
    private String dateOfOrdering;

    @DatabaseField
    private Status status;

    public Order() {
    }

    public Order(User user, String dateOfOrdering, Status status) {
        this.user = user;
        this.dateOfOrdering = dateOfOrdering;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDateOfOrdering() {
        return dateOfOrdering;
    }

    public void setDateOfOrdering(String dateOfOrdering) {
        this.dateOfOrdering = dateOfOrdering;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id &&
                Objects.equals(user, order.user) &&
                Objects.equals(dateOfOrdering, order.dateOfOrdering) &&
                status == order.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, dateOfOrdering, status);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", user=" + user +
                ", dateOfOrdering='" + dateOfOrdering + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public ArrayList<String> getQueryParams() {
        return null;
    }
}
