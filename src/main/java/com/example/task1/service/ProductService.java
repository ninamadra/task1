package com.example.task1.service;

import org.openapitools.model.ProductDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    ProductDTO createProduct(String name, LocalDate expirationDate, ProductDTO.ProductTypeDTOEnum productTypeDTO);
    Optional<ProductDTO> getProductById(Long id);
    List<ProductDTO> getAllProducts();
    boolean updateProduct(Long id, String name, LocalDate expirationDate, ProductDTO.ProductTypeDTOEnum productTypeDTO);
    boolean deleteProduct(Long id);
}
