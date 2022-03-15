package com.github.bogdan.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Objects;

@DatabaseTable(tableName = "order_item")
public class OrderItem implements Filtration{
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    private Order order;

    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    private Item item;

    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    private DeliveryCompany deliveryCompany;

    @DatabaseField
    private String deliveryDate;

    public OrderItem() {
    }

    public OrderItem(Order order, Item item, DeliveryCompany deliveryCompany, String deliveryDate) {
        this.order = order;
        this.item = item;
        this.deliveryCompany = deliveryCompany;
        this.deliveryDate = deliveryDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public DeliveryCompany getDeliveryCompany() {
        return deliveryCompany;
    }

    public void setDeliveryCompany(DeliveryCompany deliveryCompany) {
        this.deliveryCompany = deliveryCompany;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return id == orderItem.id &&
                Objects.equals(order, orderItem.order) &&
                Objects.equals(item, orderItem.item) &&
                Objects.equals(deliveryCompany, orderItem.deliveryCompany) &&
                Objects.equals(deliveryDate, orderItem.deliveryDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order, item, deliveryCompany, deliveryDate);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", order=" + order +
                ", item=" + item +
                ", deliveryCompany=" + deliveryCompany +
                ", deliveryDate='" + deliveryDate + '\'' +
                '}';
    }

    @Override
    public ArrayList<String> getQueryParams() {
        return null;
    }
}
