import com.sun.org.apache.regexp.internal.RE;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class KafkaStreans {
    private static Properties props;
    private static Properties producerprops;
    private static String SALES = "Sales";
    private static String PURCHASES = "Purchases";
    private static String SHIPMENTS = "shipmentese";
    private static String PURCHASESGO = "purchasesgo";
    private static String UNITSCOUNT = "unitscount";
    private static String AVERAGE = "average";
    private static String LASTREVENUES = "lastrevenues";
    private static String LASTREXPENSES = "lastexpenses";
    private static String RESULTS = "itemsprofit";
    private static String RESULTS2 = "itemslast";
    private static KafkaStreams streams;

    public static void main(String[] args) throws InterruptedException {
        props = putproperties();
        streams = startsteam();
        streams.setStateListener(new Listener());
        streams.start();
        Thread.sleep(10000);

        Thread thread1 = new Thread() {
            public void run() {
                receivesales();
            }
        };
        Thread thread2 = new Thread() {
            public void run() {
                receivepurchases();
            }
        };
        //thread1.start();
        //thread2.start();

    }

    public static void receivepurchases() {
        ReadOnlyKeyValueStore<String, String> keyValueStore = streams.store(RESULTS, QueryableStoreTypes.keyValueStore());
        while (true) {
            KeyValueIterator<String, String> all = keyValueStore.all();
            while (all.hasNext()) {
                KeyValue<String, String> next = all.next();
                System.out.println("Item Purchase: " + next.key + " Quantity >> " + next.value);
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            all.close();
        }

    }
    public static void receivesales() {
        ReadOnlyKeyValueStore<String, String> keyValueStore = streams.store(RESULTS, QueryableStoreTypes.keyValueStore());
        while (true) {
            KeyValueIterator<String, String> all = keyValueStore.all();
            while (all.hasNext()) {
                KeyValue<String, String> next = all.next();
                System.out.println("Item Sales: " + next.key + " Quantity >> " + next.value);
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            all.close();
        }

    }

    public static KafkaStreams startsteam(){

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String,String> sales = builder.stream(SALES);
        sales.foreach((s, sale) -> {
            System.out.println("sale revenue ->" + sale);
        });
        KStream<String,String> purchases = builder.stream(PURCHASES);
        purchases.foreach((s, purchase) -> {
            System.out.println("purchase expense ->" + purchase);
        });
        KStream<String,String> units = builder.stream("shipments-count");
        units.foreach((s, count) -> {
            System.out.println("count units->" + count);
        });

        KTable<String,String> salesprice = sales.groupByKey().reduce((oldval,newval) -> Double.toString(Double.parseDouble(oldval) + Double.parseDouble(newval)),
                Materialized.as(PURCHASESGO));
        salesprice.mapValues((wk,v) ->  "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"int32\",\"optional\":true,\"field\":\"item_id\"},{\"type\":\"double\",\"optional\":true,\"field\":\"revenue\"}],\"optional\":true},\"payload\":{\"item_id\": " + wk + ", \"revenue\":" + v + "}}").toStream().to(RESULTS);

       KTable<String,String> purchaseprice = purchases.groupByKey().reduce((oldval,newval) -> Double.toString(Double.parseDouble(oldval) + Double.parseDouble(newval)),
                Materialized.as(SHIPMENTS));
        purchaseprice.mapValues((wk,v) ->  "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"int32\",\"optional\":true,\"field\":\"item_id\"},{\"type\":\"double\",\"optional\":true,\"field\":\"expense\"}],\"optional\":true},\"payload\":{\"item_id\": " + wk + " ,\"expense\":" + v +  "}}" ).toStream().to(RESULTS);

       KTable<String,String> profit = purchaseprice.join(salesprice, ( purchasesl, salesl) -> Double.toString(Double.parseDouble(salesl)-Double.parseDouble(purchasesl)),
                Materialized.as(PURCHASES));
        profit.mapValues((wk,v) -> "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"int32\",\"optional\":true,\"field\":\"item_id\"},{\"type\":\"double\",\"optional\":true,\"field\":\"profit\"}],\"optional\":true},\"payload\":{\"item_id\": " + wk + " ,\"profit\":" + v + "}}").toStream().to(RESULTS);

       KTable<String,String> unitscount = units.groupByKey().reduce((oldval,newval) -> Integer.toString(Integer.parseInt(oldval) + Integer.parseInt(newval)),
                Materialized.as(UNITSCOUNT));
       unitscount.mapValues((wk,v) -> "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"int32\",\"optional\":true,\"field\":\"item_id\"},{\"type\":\"int32\",\"optional\":true,\"field\":\"units\"}],\"optional\":true},\"payload\":{\"item_id\": " + wk + " ,\"units\":" + v + "}}").toStream().to(RESULTS);

      KTable<String, String> averageperitem = profit.join(unitscount, (profitl,unitsl) ->  Double.toString(Double.parseDouble(profitl)/Integer.parseInt(unitsl)),
               Materialized.as(AVERAGE));
        averageperitem.mapValues((wk,v) -> "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"int32\",\"optional\":true,\"field\":\"item_id\"},{\"type\":\"double\",\"optional\":true,\"field\":\"average\"}],\"optional\":true},\"payload\":{\"item_id\": " + wk + " ,\"average\":" + v + "}}").toStream().to(RESULTS);

        KTable<Windowed<String>, String> lastrevenues = sales.groupByKey().
                windowedBy(TimeWindows.of(TimeUnit.MINUTES.toMillis(5))).reduce((oldval,newval) -> Double.toString(Double.parseDouble(oldval) + Double.parseDouble(newval)),
                Materialized.as(LASTREVENUES));
        lastrevenues.toStream((wk,v) -> wk.key()).map((k, v) -> new KeyValue<>(k, "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"int32\",\"optional\":true,\"field\":\"item_id\"},{\"type\":\"double\",\"optional\":true,\"field\":\"revenue\"}],\"optional\":true},\"payload\":{\"item_id\": " + k + ", \"revenue\":" + v + "}}")).to(RESULTS2);

        KTable<Windowed<String>, String> lastexpenses = purchases.groupByKey().
                windowedBy(TimeWindows.of(TimeUnit.MINUTES.toMillis(5))).reduce((oldval,newval) -> Double.toString(Double.parseDouble(oldval) + Double.parseDouble(newval)),
                Materialized.as(LASTREXPENSES));
        lastexpenses.toStream((wk,v) -> wk.key()).map((k, v) -> new KeyValue<>(k, "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"int32\",\"optional\":true,\"field\":\"item_id\"},{\"type\":\"double\",\"optional\":true,\"field\":\"expense\"}],\"optional\":true},\"payload\":{\"item_id\": " + k + " ,\"expense\":" + v +  "}}" )).to(RESULTS2);


        System.out.println("Reading from streams");

        return new KafkaStreams(builder.build(), props);
    }

    public static Properties putproperties(){
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "stream-application");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        return props;
    }

    static class Listener implements KafkaStreams.StateListener {


        @Override
        public void onChange(KafkaStreams.State state, KafkaStreams.State state1) {
            System.out.println("Vim do " + state + " Vou para o " + state1);
        }
    }
}
