package Enteties;

import java.io.IOException;
import java.util.*;
import java.util.Properties;
import Objects.Item;
import Objects.Country;
import Objects.Sale;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import Json.JsonDeserializer;
import Json.JsonSerializer;


public class Customer {
    private static Properties consumer_props;
    private static Properties producer_props;
    private static ArrayList<Item> items = new ArrayList<Item>();
    private static ArrayList<Country> countries = new ArrayList<Country>();
    private static Producer<String, String> producer;
    private static String topicName = "DBInfo";
    private static String resultTopic = "Sales";

    public static void main(String[] args) throws InterruptedException, IOException {
        consumer_props = consumer_props();
        producer_props = proudctor_props();
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

    private static void startconsumer() {
        JsonDeserializer<Item> itemDesiralizer = new JsonDeserializer<>(
                Item.class);
        JsonDeserializer<Country> countryDesiralizer = new JsonDeserializer<>(
                Country.class);
        KafkaConsumer<String, String> consumer =
                new KafkaConsumer<String, String>(consumer_props());
        consumer.subscribe(Collections.singletonList("DBInfo"));

        try {
            while (true) {
              //  System.out.println("plz");
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    if (record.value().contains("item_id")) {
                        Item item = itemDesiralizer.deserialize(record.topic(), record.value().getBytes());
                        if (item.getItemid() > items.size()) {
                            System.out.println("Consumed and to array: " + item.getName());
                            items.add(item);
                        }
                    } else {
                        Country country = countryDesiralizer.deserialize(record.topic(), record.value().getBytes());
                        if (country.getCountryid() > countries.size()) {
                            System.out.println("Consumed and to array: " + country.getName());
                            countries.add(country);
                        }
                    }

                }
            }
        } finally {
            consumer.close();
        }
    }


    private static void startproducer(){
        while(true) {
           try {
               if(items.size()!= 0 && countries.size() != 0) {
                   Sale sale = generateitemsale();
                   System.out.println("Produced: " + sale.getName());
                   System.out.println(Double.toString(sale.getPrice()));
                   producer = new KafkaProducer<>(producer_props);
                   producer.send(new ProducerRecord<>(resultTopic, Integer.toString(sale.getItemid()) , Double.toString(sale.getPrice()) ));
                   producer.flush();
               }
               Thread.sleep(10000);
           } catch (Exception e) {
             // System.out.println(e.getMessage());
           }}
    //   }
    }
    private static Sale generateitemsale(){
        Random random = new Random();
        int randomitem = random.nextInt((items.size() + 1)) ;
        int randomcountrie = random.nextInt(countries.size()) + 1;
        int units = random.nextInt((10 - 1) + 1) + 1;
        Double priceunit = 10.0 + (500.0 - 10.0) * random.nextDouble();
        Double totalprice = priceunit * units;
        Sale sale = new Sale(randomitem, randomcountrie , items.get(randomitem - 1).getName(), totalprice,units );
        return sale;

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

    private static Properties proudctor_props(){
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

        producer_props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        producer_props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");


        return producer_props;
    }
}
