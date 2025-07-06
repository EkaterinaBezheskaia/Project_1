package com.store.services;

import com.api.dto.ProductDTO;
import com.api.mappers.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.store.entities.ProductEntity;
import com.store.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductDTO addProduct(ProductDTO product) {
        ProductEntity productEntity = productMapper.toProductEntity(product);
        ProductEntity savedProductEntity = productRepository.save(productEntity);
        return productMapper.toProductDTO(savedProductEntity);
    }

    public ProductDTO updateProduct(ProductDTO product) {
        ProductEntity productEntity = productMapper.toProductEntity(product);
        ProductEntity savedProductEntity = productRepository.save(productEntity);
        return productMapper.toProductDTO(savedProductEntity);
    }

    public List<ProductDTO> getAllProducts() {
        List<ProductDTO> productDTOS = new ArrayList<>();
        productRepository.findAll().forEach(productEntity -> {
            productDTOS.add(productMapper.toProductDTO(productEntity));
        });
        return productDTOS;
    }

    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }
}
