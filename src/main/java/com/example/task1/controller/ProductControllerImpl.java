package com.example.task1.controller;

import com.example.task1.service.ProductService;
import org.openapitools.api.ProductsApi;
import org.openapitools.model.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class ProductControllerImpl implements ProductsApi {

    private final ProductService productService;

    //w nowym spring adnotacja @Autowired nie jest już konieczna
    @Autowired
    public ProductControllerImpl(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public ResponseEntity<ProductDTO> createProduct(ProductDTO productDTO) {
        //TODO ja bym przekazywał cały obiekt ProductDTO do serwisu i tam wyciągał wartości
        ProductDTO response = productService.createProduct(
                productDTO.getName(),
                productDTO.getExpirationDate(),
                productDTO.getProductTypeDTO());
        return ResponseEntity.created(URI.create("/" + response.getId())).body(response);
    }

    @Override
    public ResponseEntity<Void> deleteProductById(Long id) {
        if(productService.deleteProduct(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @Override
    public ResponseEntity<ProductDTO> getProductById(Long id) {
        Optional<ProductDTO> result = productService.getProductById(id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<ProductDTO> updateProduct(Long id, ProductDTO productDTO) {
        if (productService.updateProduct(
                id,
                productDTO.getName(),
                productDTO.getExpirationDate(),
                productDTO.getProductTypeDTO())) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
