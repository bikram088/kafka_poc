package com.kafka.connector;

import com.kafka.connector.service.ScheduledTaskService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@EnableKafka
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, topics = {"my-kafka-topic"})
public class KafkaIntegrationTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private ScheduledTaskService scheduledTaskService;

    private KafkaConsumer<String, String> consumer;

    @BeforeEach
    public void setup() {
        consumer = new KafkaConsumer<>(KafkaTestUtils.consumerProps("test-consumer-group", "false", embeddedKafkaBroker));
    }

    @Test
    public void testProduceConsume() {
        String topic = "my-kafka-topic";

        scheduledTaskService.fetchDataAndSendToKafka();

        // Consume message from Kafka topic
        consumer.subscribe(Collections.singletonList(topic));
        ConsumerRecord<String, String> record = KafkaTestUtils.getSingleRecord(consumer, topic);

        // Validate the consumed message
        assertTrue(record.value().contains("id"));
        assertTrue(record.value().contains("title"));
        assertTrue(record.value().contains("body"));
    }
}