package com.kafka.connector.model;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Post {
    private Integer id;
    @NotNull
    private String title;
    private String body;

    @Override
    public String toString() {
        return "Post{id=" + id + ", title='" + title + "', body='" + body + "'}";
    }
}