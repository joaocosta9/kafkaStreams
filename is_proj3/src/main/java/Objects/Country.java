package Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;
@JsonRootName("countries")
public class Country implements Serializable {
    @JsonProperty("country_id")
    int countryid;
    @JsonProperty("country_name")
    String name;

    public Country(@JsonProperty("country_id") int countryid,  @JsonProperty("country_name")String name) {
        this.countryid = countryid;
        this.name = name;
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
}
