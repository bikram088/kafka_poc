package com.kafka.connector.service;

import com.kafka.connector.external.clients.JsonPlaceholderClient;
import com.kafka.connector.external.exceptions.ApiException;
import com.kafka.connector.model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ScheduledTaskService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTaskService.class);

    private final KafkaProducerService kafkaProducerService;
    private final JsonPlaceholderClient jsonPlaceholderClient;

    private static final String VALID_EVENT_TOPICS = "my-kafka-topic";

    String isTest = "${test}";
    @Autowired
    public ScheduledTaskService(KafkaProducerService kafkaProducerService, JsonPlaceholderClient jsonPlaceholderClient) {
        this.kafkaProducerService = kafkaProducerService;
        this.jsonPlaceholderClient = jsonPlaceholderClient;
    }

    @Scheduled(cron = "${cron.schedule}")
    public void fetchDataAndSendToKafka() {
        List<Post> posts = new ArrayList<>();
        try {
            posts = jsonPlaceholderClient.getPosts().getBody();
        } catch (ApiException ex) {
            logger.error("Exception encountered when calling the API with error {}.", ex.getMessage());
        }

            //For Testing purpose please comment out the below line
            posts.forEach(this::processPost);
            processPost(posts.get(0));
    }

    private void processPost(Post post) {
        if (isValidPost(post)) {
            kafkaProducerService.sendMessage(VALID_EVENT_TOPICS, post);
            logger.info("Produced message to Kafka: {}.", post);
        } else {
            logger.info("Invalid Data: {}.", post);
        }
    }

    private boolean isValidPost(Post post) {
        return post.getTitle() != null && !post.getTitle().isEmpty();
    }
}