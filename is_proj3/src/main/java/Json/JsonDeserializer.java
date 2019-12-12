package Json;

//import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class JsonDeserializer<T> implements Deserializer<T> {

    private ObjectMapper om = new ObjectMapper();
    private Class<T> type;

    /*
     * Default constructor needed by kafka
     */
    public JsonDeserializer() {

    }

    public JsonDeserializer(Class<T> type) {
        this.type = type;
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub

    }

    @SuppressWarnings("unchecked")
    @Override
    public void configure(Map<String, ?> map, boolean arg1) {
        if (type == null) {
            type = (Class<T>) map.get("type");
        }

    }

    @Override
    public T deserialize(String undefined, byte[] bytes) {
        T data = null;
        int count = 0;
        String s = new String(bytes);
        JsonObject jsonObject = new JsonParser().parse(s).getAsJsonObject();
        String a = jsonObject.get("payload").toString();
        byte [] b = a.getBytes();

        try {
            data = om.readValue(b, type);
        } catch (Exception e) {
            throw new SerializationException(e);
        }

        return data;
    }

    protected Class<T> getType() {
        return type;
    }

}