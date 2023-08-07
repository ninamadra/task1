package com.example.task1;

import com.example.task1.service.ProductService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ProductKafkaConsumer {

    private final ProductService productService;

    public ProductKafkaConsumer(ProductService productService) {
        this.productService = productService;
    }

    @KafkaListener(topics = "store_status", groupId = "product_group")
    public void consumeProductMessage(ProductMessage productMessage) {
        Long productId = productMessage.getId();
        Integer quantity = productMessage.getQuantity();

        productService.updateProductQuantity(productId, quantity);
    }
}
