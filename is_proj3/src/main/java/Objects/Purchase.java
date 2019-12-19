package Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Purchase {
    @JsonProperty("item_id")
    int itemid;
    @JsonProperty("item_name")
    String name;
    @JsonProperty("item_price")
    Double price;
    @JsonProperty("items_units")
    int units;

    public Purchase(@JsonProperty("item_id") int itemid,@JsonProperty("item_name") String name, @JsonProperty("item_price") Double price, @JsonProperty("items_units") int units) {
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
