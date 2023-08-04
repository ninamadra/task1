package com.example.task1.service;

import com.example.task1.mapper.ProductMapper;
import com.example.task1.model.Product;
import com.example.task1.repository.ProductRepository;
import org.openapitools.model.ProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductDTO createProduct(String name, LocalDate expirationDate, ProductDTO.ProductTypeDTOEnum productTypeDTO) {
        Product created = Product.builder()
                .name(name)
                .expirationDate(expirationDate)
                .productType((productMapper.toProductType(productTypeDTO)))
                .build();
        //TODO mozna nadpisac metode toString w klasie Product
        logger.info(created.getId() + " " + created.getName() + " " +  created.getExpirationDate() + " " + created.getProductType());

        //TODO nie ma potrzeby tworzenia obiektu. Mozna od razu dodac do metody toDTO
        Product savedProduct = productRepository.save(created);
        return productMapper.toDTO(savedProduct);

    }

    @Override
    public Optional<ProductDTO> getProductById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        return productOptional.map(productMapper::toDTO);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toDTO)
                //TODO uzyj .toList zamiast collect
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateProduct(Long id, String name, LocalDate expirationDate, ProductDTO.ProductTypeDTOEnum productTypeDTO) {

        Optional<Product> productOptional = productRepository.findById(id);

        if(productOptional.isPresent()){
            Product toUpdate = productOptional.get();
            if(name != null) {
                toUpdate.setName(name);
            }
            if(expirationDate != null) {
                toUpdate.setExpirationDate(expirationDate);
            }
            if( productTypeDTO != null) {
                toUpdate.setProductType(productMapper.toProductType(productTypeDTO));
            }

            productRepository.save(toUpdate);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteProduct(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        //TODO od razu zwracamy. Nie robimy zbędnych obiektów
        boolean isFound = productOptional.isPresent();
        productOptional.ifPresent(productRepository::delete);
        return isFound;

    }
}
