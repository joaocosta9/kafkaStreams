package Enteties;
import java.io.IOException;
import java.util.*;
import java.util.Properties;
import Objects.Item;
import Objects.ItemOrCountry;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.StreamsBuilder;
import Json.JsonDeserializer;
import Json.JsonSerializer;
import org.mortbay.util.ajax.JSON;


public class Supplier {
    private static Properties consumer_props;
    private static Properties producer_props;
    private static KafkaConsumer<String, ItemOrCountry> consumer;
    private static ArrayList<ItemOrCountry> items = new ArrayList<ItemOrCountry>();
    private static ArrayList<ItemOrCountry> countries = new ArrayList<ItemOrCountry>();
    private static Producer<String, Item> producer;
    private static String topicName = "DBInfo";
    private static String resultTopic = "Purchases";

    public static void main(String[] args) {
        consumer_props = consumer_props();
        producer_props = producer_props();
        Thread thread1 = new Thread () {
            public void run () {
                startconsumer();
            }
        };
        Thread thread2 = new Thread () {
            public void run () {
                startproducer();
            }
        };
        thread1.start();
        thread2.start();


    }

    private static void startconsumer(){

        JsonSerializer <ItemOrCountry> itemsale = new JsonSerializer<>();
        JsonDeserializer<ItemOrCountry> itemsalej  = new JsonDeserializer<>(
                ItemOrCountry.class);

        StreamsBuilder builder = new StreamsBuilder();
        KafkaConsumer<String, String> consumer =
                new KafkaConsumer<String, String>(consumer_props());
        consumer.subscribe(Collections.singletonList("DBInfo"));
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records)
                {
                    ItemOrCountry item =  itemsalej.deserialize(record.topic(),record.value().getBytes());
                    items.add(item);
                    System.out.println(item.getItem_id());

                }
            }
        } finally {
            consumer.close();
        }
    }

    private static void startproducer(){
        ItemOrCountry country = new ItemOrCountry(20,null,"Italy", 10);
        countries.add(country);
        while(true) {
            //if(items.size()!= 0 && countries.size() != 0){
            try {
                // System.out.println("entrei");
                Item itempurchase = generatepurchase();
                producer = new KafkaProducer<>(producer_props);
                producer.send(new ProducerRecord<>(resultTopic, "items_purchase", itempurchase));
                producer.flush();
                Thread.sleep(10000);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }}
    }

    private static Item generatepurchase(){
        Random random = new Random();
        int randomitem = random.nextInt((items.size() + 1)) ;
        //int randomcountrie = random.nextInt((countries.size() + 1) + 1) ;
        int units = random.nextInt((10 - 1) + 1) + 1;
        Double priceunit = 10.0 + (300.0 - 10.0) * random.nextDouble();
        Double totalprice = priceunit * units;
        Item item = new Item(randomitem, items.get(randomitem - 1).getItem_name(), totalprice,units );
        return item;
    }




    private static Properties consumer_props(){
        consumer_props = new Properties();
        consumer_props.put(StreamsConfig.APPLICATION_ID_CONFIG, "exercises-application");
        consumer_props.put("bootstrap.servers", "localhost:9092");
        consumer_props.put("group.id", "suplliers");
        consumer_props.put("enable.auto.commit", "true");
        consumer_props.put("auto.commit.interval.ms", "1000");
        consumer_props.put("session.timeout.ms", "30000");
        consumer_props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        consumer_props.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        return consumer_props;
    }

    private static Properties producer_props(){
        producer_props = new Properties();

        //Assign localhost id
        producer_props.put("bootstrap.servers", "localhost:9092");

        //Set acknowledgements for producer requests.
        producer_props.put("acks", "all");

        //If the request fails, the producer can automatically retry,
        producer_props.put("retries", 0);

        //Specify buffer size in config
        producer_props.put("batch.size", 16384);

        //Reduce the no of requests less than 0
        producer_props.put("linger.ms", 1);

        //The buffer.memory controls the total amount of memory available to the producer for buffering.
        producer_props.put("buffer.memory", 33554432);

        producer_props.put(ProducerConfig.CLIENT_ID_CONFIG,
                "kafka json producer");
        //producer_props.put("value.serializer", "com.knoldus.serializers.UserSerializer");
        producer_props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producer_props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,JsonSerializer.class);


        return producer_props;
    }

}
