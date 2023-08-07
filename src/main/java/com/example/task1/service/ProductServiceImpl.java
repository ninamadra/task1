package com.example.task1.service;

import com.example.task1.ProductKafkaProducer;
import com.example.task1.mapper.ProductMapper;
import com.example.task1.model.Product;
import com.example.task1.repository.ProductRepository;
import org.openapitools.model.ProductReadDTO;
import org.openapitools.model.ProductWriteDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductKafkaProducer kafkaProducer;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper, ProductKafkaProducer kafkaProducer) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public ResponseEntity<ProductReadDTO> createProduct(ProductWriteDTO productWriteDTO) {
        Product product = productMapper.toEntity(productWriteDTO);
        productRepository.save(product);
        kafkaProducer.sendProductMessage(product.getId());
        return ResponseEntity.created(URI.create("/" + product.getId())).body(productMapper.toReadDTO(product));
    }

    @Override
    public ResponseEntity<ProductReadDTO> getProductById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);

        return productOptional.map(product -> ResponseEntity.ok(productMapper.toReadDTO(product)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<ProductReadDTO>> getAllProducts() {
        List<ProductReadDTO> products =  productRepository.findAll().stream()
                .map(productMapper::toReadDTO)
                .toList();
        return ResponseEntity.ok(products);
    }

    @Override
    public ResponseEntity<Void> deleteProductById(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<ProductReadDTO> updateProduct(Long id, ProductWriteDTO productWriteDTO) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product productToUpdate = productMapper.toEntity(productWriteDTO);
            productToUpdate.setId(id);
            Product updatedProduct = productRepository.save(productToUpdate);
            return ResponseEntity.ok(productMapper.toReadDTO(updatedProduct));
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public void updateProductQuantity(Long productId, Integer quantity) {
        Optional<Product> productOptional = productRepository.findById(productId);
        productOptional.ifPresent(product -> {
            product.setQuantity(quantity);
            productRepository.save(product);
        });
    }

}
