package com.kafka.connector.external.clients;

import com.kafka.connector.model.Post;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import java.util.List;

@FeignClient(name = "jsonplaceholder", url = "${sample.web.service.url}")
public interface JsonPlaceholderClient {

    @GetMapping("/posts")
    ResponseEntity<List<Post>> getPosts();
}