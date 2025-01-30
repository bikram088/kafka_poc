package com.kafka.demo.service;

import com.kafka.demo.model.Post;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "my-kafka-topic", groupId = "my_group", containerFactory = "postKafkaListenerFactory")
    public void consume(Post post) {
        System.out.println("Consumed message: " + post);
    }
}
