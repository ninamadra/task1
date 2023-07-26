package com.example.task1.mapper;

import com.example.task1.model.Product;
import com.example.task1.model.ProductType;
import org.modelmapper.ModelMapper;
import org.openapitools.model.ProductDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    private final ModelMapper modelMapper;

    public ProductMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Product toEntity(ProductDTO generatedProduct) {
        return generatedProduct != null ? modelMapper.map(generatedProduct, Product.class) : null;
    }

    public ProductDTO toDTO(Product product) {
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        productDTO.setProductTypeDTO(ProductDTO.ProductTypeDTOEnum.valueOf(product.getProductType().name()));
        return productDTO;
    }


    public ProductType toProductType(ProductDTO.ProductTypeDTOEnum productTypeDTO) {
        return modelMapper.map(productTypeDTO, ProductType.class);
    }

    public ProductDTO.ProductTypeDTOEnum toProductTypeDTO(ProductType productType) {
        return ProductDTO.ProductTypeDTOEnum.valueOf(productType.name());
    }

}



