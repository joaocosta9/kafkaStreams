package Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Sale {
    @JsonProperty("item_id")
    int itemid;
    @JsonProperty("country_id")
    int countryid;
    @JsonProperty("item_name")
    String name;
    @JsonProperty("item_price")
    Double price;
    @JsonProperty("items_units")
    int units;

    public Sale(@JsonProperty("item_id") int itemid, @JsonProperty("country_id") int countryid, @JsonProperty("item_name") String name,
                @JsonProperty("item_price") Double price,  @JsonProperty("items_units")int units) {
        this.itemid = itemid;
        this.countryid = countryid;
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
