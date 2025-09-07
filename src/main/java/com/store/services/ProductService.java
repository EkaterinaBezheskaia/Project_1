package com.store.services;

import com.api.dto.ProductDTO;
import com.api.mappers.ProductMapper;
import com.store.specifications.ClientSpecification;
import com.store.specifications.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.store.entities.ProductEntity;
import com.store.repositories.ProductRepository;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @ResponseStatus(HttpStatus.CONFLICT)
    public ProductDTO addProduct(ProductDTO product) {
        if (productRepository.existsByNameAndDescription(product.getName(), product.getDescription())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Продукт с таким названием и описанием уже существует");
        }
        ProductEntity productEntity = productMapper.toProductEntity(product);
        return productMapper.toProductDTO(productRepository.save(productEntity));
    }

    public ProductDTO updateProduct(int id, ProductDTO product) {

        ProductEntity productEntity = productRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не найден"));

        if (productRepository.existsByNameAndDescription(product.getName(), product.getDescription())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Продукт с таким названием и описанием уже существует");
        }

        return productMapper.toProductDTO(productRepository.save(productEntity));
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> getAllProducts(String name, String description, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {

        Specification<ProductEntity> spec =
                ProductSpecification.nameContains(name)
                        .and(ProductSpecification.descriptionContains(description))
                        .and(ProductSpecification.priceBetween(minPrice, maxPrice));
        return productRepository.findAll(spec, pageable)
                .map(productMapper::toProductDTO);

    }

    public void deleteProduct(int id) {
        if (!productRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет продукта с запрошенным id");
        }
        productRepository.deleteById(id);
    }
}
