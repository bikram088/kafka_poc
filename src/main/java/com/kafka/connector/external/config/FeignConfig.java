package com.kafka.connector.external.config;

import com.kafka.connector.external.exceptions.ApiException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import feign.codec.ErrorDecoder;

@Configuration
@PropertySource("classpath:application.properties")
public class FeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new ErrorDecoder.Default() {
            @Override
            public Exception decode(String methodKey, feign.Response response) {
                if (response.status() != 200) {
                    return new ApiException("Error calling API, status code: " + response.status());
                }
                return super.decode(methodKey, response);
            }
        };
    }
}