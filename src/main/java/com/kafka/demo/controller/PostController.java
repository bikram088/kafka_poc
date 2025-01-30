package com.kafka.demo.controller;


import com.kafka.demo.service.PostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // Endpoint to trigger sending posts to Kafka
    @GetMapping("/send-posts-to-kafka")
    public String sendPostsToKafka() {
        postService.sendValidPostsToKafka();
        return "Posts have been sent to Kafka!";
    }
}