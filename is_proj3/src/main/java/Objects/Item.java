package Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("items")
public class Item {
    @JsonProperty("item_id")
    int itemid;
    @JsonProperty("item_name")
    String name;

    public Item(@JsonProperty("item_id") int itemid, @JsonProperty("item_name") String name) {
        this.itemid = itemid;
        this.name = name;
    }

    public int getItemid() {
        return itemid;
    }


    public String getName() {
        return name;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public void setName(String name) {
        this.name = name;
    }
}
