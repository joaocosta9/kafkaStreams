package Objects;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("itemsorcountry")
public class ItemOrCountry {
    @JsonProperty("item_id")
    int item_id;
    @JsonProperty("country_id")
    int country_id;
    @JsonProperty("item_name")
    String item_name;
    @JsonProperty("country_name")
    String country_name;

    @JsonCreator
    public ItemOrCountry(@JsonProperty("item_id") int item_id,@JsonProperty("item_name") String item_name, @JsonProperty("country_name") String country_name,@JsonProperty("country_id") int country_id) {
        this.item_id = item_id;
        this.item_name = item_name;
        this.country_name = country_name;
        this.country_id = country_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }
}
