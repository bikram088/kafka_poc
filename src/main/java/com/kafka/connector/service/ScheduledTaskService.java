package com.kafka.connector.service;

import com.kafka.connector.external.clients.JsonPlaceholderClient;
import com.kafka.connector.external.exceptions.ApiException;
import com.kafka.connector.model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class ScheduledTaskService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTaskService.class);

    private final KafkaProducerService kafkaProducerService;
    private final JsonPlaceholderClient jsonPlaceholderClient;

    private static final String VALID_EVENT_TOPICS = "my-kafka-topic";
    //private static final String FAILED_EVENTS_TOPICS = "my-kafka-topic";


    @Autowired
    public ScheduledTaskService(KafkaProducerService kafkaProducerService, JsonPlaceholderClient jsonPlaceholderClient) {
        this.kafkaProducerService = kafkaProducerService;
        this.jsonPlaceholderClient = jsonPlaceholderClient;
    }

    @Scheduled(cron = "*/5 * * * * *")
    public void fetchDataAndSendToKafka() {
        List<Post> posts = new ArrayList<>();
        try {
            posts = jsonPlaceholderClient.getPosts().getBody();
        }
        catch(ApiException ex){
            logger.error("Exception Encountered When calling the API with error {}.", ex.getMessage());
        }

        for (Post post : posts) {
            // Validate the data
            if (post.getTitle() != null && !post.getTitle().isEmpty()) {
                // Send the valid post data to Kafka
                kafkaProducerService.sendMessage(VALID_EVENT_TOPICS, post);
                logger.info("Produced message to Kafka: {}.", post);
            } else {
                logger.info("Invalid Data: {}.", post);
            }
        }
    }
}
