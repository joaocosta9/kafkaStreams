package Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("items")
public class Item {
    int itemid;
    int countryid;
    String name;
    Double price;
    int units;

    public Item(int itemid, int countryid, String name,Double price,int units) {
        this.itemid = itemid;
        this.countryid = countryid;
        this.name = name;
        this.price = price;
        this.units = units;
    }

    public Item(int itemid, String name) {
        this.itemid = itemid;
        this.name = name;
    }

    public Item(int itemid, String name, Double price, int units) {
        this.itemid = itemid;
        this.name = name;
        this.price = price;
        this.units = units;
    }

    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public int getCountryid() {
        return countryid;
    }

    public void setCountryid(int countryid) {
        this.countryid = countryid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }
}
