package com.example.task1;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProductKafkaProducer {

    private final KafkaTemplate<String, Long> kafkaTemplate;
    private static final String TOPIC_NAME = "store_control";

    public ProductKafkaProducer(KafkaTemplate<String, Long> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendProductMessage(Long productId) {
        kafkaTemplate.send(TOPIC_NAME, productId);
    }
}
