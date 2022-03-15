package com.github.bogdan.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Objects;

@DatabaseTable(tableName = "notifications")
public class Notifications {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(foreignAutoRefresh = true,foreign = true)
    private User user;
    @DatabaseField
    private String message;
    @DatabaseField
    private boolean watched;

    public Notifications() {
    }

    public Notifications(User user, String message, boolean watched) {
        this.user = user;
        this.message = message;
        this.watched = watched;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notifications that = (Notifications) o;
        return id == that.id &&
                watched == that.watched &&
                Objects.equals(user, that.user) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, message, watched);
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
