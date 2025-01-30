package com.kafka.demo.service;
import com.kafka.demo.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class KafkaProducerService {

    private static final String TOPIC = "my-kafka-topic";

    @Autowired
    private KafkaTemplate<String, Post> kafkaTemplate; // Use <String, Post> instead of <String, String>

    public void sendMessage(Post post) {
        kafkaTemplate.send(TOPIC, post); // ✅ Send full Post object instead of post.getTitle()
        System.out.println("Produced message: " + post);
    }
}