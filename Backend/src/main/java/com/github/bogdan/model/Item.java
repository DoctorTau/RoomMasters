package com.github.bogdan.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Objects;

@DatabaseTable(tableName = "item")
public class Item implements Filtration{
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String img;

    @DatabaseField
    private String model;

    @DatabaseField
    private double price;

    public Item() {
    }

    public Item(String img, String model, double price) {
        this.img = img;
        this.model = model;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id &&
                Double.compare(item.price, price) == 0 &&
                Objects.equals(img, item.img) &&
                Objects.equals(model, item.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, img, model, price);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", img='" + img + '\'' +
                ", model='" + model + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public ArrayList<String> getQueryParams() {
        return null;
    }
}
