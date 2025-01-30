package com.kafka.demo.service;

import com.kafka.demo.model.Post;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final RestTemplate restTemplate;
    private final KafkaProducerService kafkaProducerService;

    public PostService(RestTemplate restTemplate, KafkaProducerService kafkaProducerService) {
        this.restTemplate = restTemplate;
        this.kafkaProducerService = kafkaProducerService;
    }

    // Fetch posts from the API
    public List<Post> fetchPosts() {
        String url = "https://jsonplaceholder.typicode.com/posts";
        ResponseEntity<List<Post>> response = restTemplate.exchange(url,
                org.springframework.http.HttpMethod.GET, null,
                new org.springframework.core.ParameterizedTypeReference<List<Post>>() {});
        return response.getBody();
    }

    // Validate data and filter out invalid posts
    public List<Post> fetchValidPosts() {
        List<Post> posts = fetchPosts();
        return posts.stream()
                .filter(post -> post.getTitle() != null && !post.getTitle().isEmpty())
                .collect(Collectors.toList());
    }

    // Send valid posts to Kafka
    public void sendValidPostsToKafka() {
        List<Post> validPosts = fetchValidPosts();
        validPosts.forEach(kafkaProducerService::sendMessage);
    }
}