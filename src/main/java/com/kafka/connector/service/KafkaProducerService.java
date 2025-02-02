package com.kafka.connector.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.connector.model.Post;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, Post post) {
        try {
            String jsonPost = new ObjectMapper().writeValueAsString(post);
            kafkaTemplate.send(topic, jsonPost);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize Post object", e);
        }
    }
}