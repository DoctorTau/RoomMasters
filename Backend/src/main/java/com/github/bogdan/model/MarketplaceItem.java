package com.github.bogdan.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Objects;

@DatabaseTable(tableName = "marketplace_item")
public class MarketplaceItem implements Filtration{
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    private Marketplace marketplace;

    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    private Item item;

    public MarketplaceItem() {
    }

    public MarketplaceItem(Marketplace marketplace, Item item) {
        this.marketplace = marketplace;
        this.item = item;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Marketplace getMarketplace() {
        return marketplace;
    }

    public void setMarketplace(Marketplace marketplace) {
        this.marketplace = marketplace;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarketplaceItem that = (MarketplaceItem) o;
        return id == that.id &&
                Objects.equals(marketplace, that.marketplace) &&
                Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, marketplace, item);
    }

    @Override
    public ArrayList<String> getQueryParams() {
        return null;
    }
}
