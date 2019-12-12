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
import org.apache.kafka.streams.StreamsConfig;
import Json.JsonDeserializer;
import Json.JsonSerializer;


public class Customer {
    private static Properties consumer_props;
    private static Properties producer_props;
    private static ArrayList<ItemOrCountry> items = new ArrayList<ItemOrCountry>();
    private static ArrayList<ItemOrCountry> countries = new ArrayList<ItemOrCountry>();
    private static Producer<String, Item> producer;
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

    private static void startconsumer(){

        JsonSerializer <ItemOrCountry> itemorcountryJsonSerializer = new JsonSerializer<>();
        JsonDeserializer<ItemOrCountry> itemorcountryJsonDesiralizer = new JsonDeserializer<>(
                ItemOrCountry.class);
        KafkaConsumer<String, String> consumer =
                new KafkaConsumer<String, String>(consumer_props());
        consumer.subscribe(Collections.singletonList("DBInfo"));
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records)
                {
                    ItemOrCountry item =  itemorcountryJsonDesiralizer.deserialize(record.topic(),record.value().getBytes());
                    if(item.getItem_name() != null) {
                        //System.out.println(item.getItem_name());
                        if(item.getItem_id() > items.size()) {
                            System.out.println(item.getItem_name());
                            items.add(item);
                        }
                    }else{
                        if(item.getCountry_id() > countries.size()){
                            System.out.println(item.getCountry_name());
                            countries.add(item);
                        }
                    }

                }
            }
        } finally {
            consumer.close();
        }


        /*Serde<ItemOrCountry> itemorcountrySerde = Serdes.serdeFrom(itemorcountryJsonSerializer,
                itemorcountryJsonDesiralizer);

        StreamsBuilder builder = new StreamsBuilder();
        try {
            KStream<String, ItemOrCountry> kStream = builder.stream(topicName, Consumed.with(Serdes.String(), itemorcountrySerde));
            kStream.foreach((s, itemOrCountry) -> {
                if(itemOrCountry.getItem_name() != null) {
                    if(itemOrCountry.getItem_id() > items.size()) {
                        System.out.println(itemOrCountry.getItem_name());
                        items.add(itemOrCountry);
                    }
                }else{
                    if(itemOrCountry.getCountry_id() > countries.size()) {
                        System.out.println(itemOrCountry.getCountry_name());
                        countries.add(itemOrCountry);
                    }
                }
            });
        }catch (Exception s){
            s.printStackTrace();
        }



        KafkaStreams streams = new KafkaStreams(builder.build(), consumer_props());
        streams.start();
        System.out.println("Reading stream from topic " + topicName);*/
    }

    private static void startproducer(){
        while(true) {
           try {
               if(items.size()!= 0 && countries.size() != 0) {
                   System.out.println(countries.size());
                   Item itemsale = generateitemsale();
                   producer = new KafkaProducer<>(producer_props);
                   producer.send(new ProducerRecord<>(resultTopic, "items_sales", itemsale));
                   producer.flush();
               }
               Thread.sleep(5000);
           } catch (Exception e) {
             // System.out.println(e.getMessage());
           }}
    //   }
    }

    private static Item generateitemsale(){
        Random random = new Random();
        int randomitem = random.nextInt((items.size() + 1)) ;
        int randomcountrie = random.nextInt(countries.size()) + 1;
        int units = random.nextInt((10 - 1) + 1) + 1;
        Double priceunit = 10.0 + (500.0 - 10.0) * random.nextDouble();
        Double totalprice = priceunit * units;
        Item item = new Item(randomitem, randomcountrie , items.get(randomitem - 1).getItem_name(), totalprice,units );
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

        producer_props.put(ProducerConfig.CLIENT_ID_CONFIG,
                "kafka json producer");
        //producer_props.put("value.serializer", "com.knoldus.serializers.UserSerializer");
        producer_props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producer_props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,JsonSerializer.class);


        return producer_props;
    }
}
