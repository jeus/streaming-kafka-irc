/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datis.irc.botProducer;

import com.datis.irc.pojo.JsonPOJODeserializer;
import com.datis.irc.pojo.JsonPOJOSerializer;
import com.datis.irc.pojo.UserMessages;
import com.google.gson.Gson;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.jibble.pircbot.PircBot;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

/**
 *
 * @author jeus
 */
public class MyBot extends PircBot {

    int count = 1;
    private final KafkaProducer<Integer, String> producer;
    private final String topic;
    private final Boolean isAsync;
    IntegerSerializer intSerializer = new IntegerSerializer();

    public MyBot() {
//        this pr = new Producer("test1", true);
//        pr.run();
        this.setName("jeus2");
        final Serializer<UserMessages> userMessageSer = new JsonPOJOSerializer<>();
        Properties props = new Properties();
        props.put("bootstrap.servers", "172.17.0.11:9092");
        props.put("client.id", "DemoProducer");
//        props.put("batch.size",150);//this for async by size in buffer
//        props.put("linger.ms", 9000);//this for async by milisecond messages buffered
        props.put("acks", "1");
//        props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
//        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer(props, intSerializer, userMessageSer);
//        producer = new KafkaProducer<>(props);
        this.topic = "irc-messages-log";
        this.isAsync = true;
    }

    Map<String, Object> serdeProps = new HashMap<>();

    private void produce(String channel, String sender, String login, String hostname, String message) {
        UserMessages messages = new UserMessages();
        messages.channel = channel;
        messages.hostName = hostname;
        messages.login = login;
        messages.message = message;
        messages.timestamp = (new Date()).getTime();
        messages.user = sender;

        Gson gson = new Gson();
        String json = gson.toJson(messages);
        System.out.println(json);

        count++;

        long startTime = System.currentTimeMillis();
        System.out.println("Count:" + count);
        if (isAsync) { // Send asynchronously
            System.out.println("************************ASYNC SEND");
            Date newDt = new Date();
            Long time = newDt.getTime();
            DemoCallBack back = new DemoCallBack(startTime, count, json);
            producer.send(new ProducerRecord(topic, count, messages), back);
            System.out.println("BACK-----" + back.toString());
        } else { // Send synchronously
            System.out.println("#########################SYNC SEND");
            try {
                Date newDt = new Date();
                producer.send(new ProducerRecord<>(topic, count, json)).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

    }

    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        for (int i = 1; i < 30; i++) {
            try {
                produce(channel, sender, login, hostname, message);
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                Logger.getLogger(MyBot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    class DemoCallBack implements Callback {

        private final long startTime;
        private final int key;
        private final String message;

        public DemoCallBack(long startTime, int key, String message) {
            this.startTime = startTime;
            this.key = key;
            this.message = message;
        }

        /**
         * A callback method the user can implement to provide asynchronous
         * handling of request completion. This method will be called when the
         * record sent to the server has been acknowledged. Exactly one of the
         * arguments will be non-null.
         *
         * @param metadata The metadata for the record that was sent (i.e. the
         * partition and offset). Null if an error occurred.
         * @param exception The exception thrown during processing of this
         * record. Null if no error occurred.
         */
        @Override
        public void onCompletion(RecordMetadata metadata, Exception exception) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (metadata != null) {
                System.out.println("message(" + key + ", " + message + ") sent to partition(" + metadata.partition() + "), " + "offset(" + metadata.offset() + ") in " + elapsedTime + " ms");
            } else {
                exception.printStackTrace();
            }
        }
    }
}
