package com.store.services;

import com.api.dto.ProductDTO;
import com.api.mappers.ProductMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.store.entities.ProductEntity;
import com.store.repositories.ProductRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @ResponseStatus(HttpStatus.CONFLICT)
    public ProductDTO addProduct(ProductDTO product) {

        if (product.getName() == null || product.getName().isBlank()) {
            throw new IllegalArgumentException("Название обязательно");
        }
        if (product.getDescription() == null || product.getDescription().isBlank()) {
            throw new IllegalArgumentException("Описание обязательно");
        }
        if (product.getPrice() == null || product.getPrice() < 0) {
            throw new IllegalArgumentException("Цена обязательна");
        }

        if (productRepository.existsByName(product.getName())) {
            throw new DataIntegrityViolationException("Название должно быть уникальным");
        }

        ProductEntity productEntity = productMapper.toProductEntity(product);
        return productMapper.toProductDTO(productRepository.save(productEntity));
    }

    public ProductDTO updateProduct(int id, String description) {

        ProductEntity productEntity = productRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Товар не найден"));

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Описание обязательно");
        }

        if (Objects.equals(productEntity.getDescription(), description)) {
            return productMapper.toProductDTO(productEntity);
        }
        productEntity.setDescription(description);

        return productMapper.toProductDTO(productRepository.save(productEntity));
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> getAllProducts(String name, String description, Long price, Pageable pageable) {

        Specification<ProductEntity> spec = ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            addTextPredicate(predicates, criteriaBuilder, root, "name", name);
            addTextPredicate(predicates, criteriaBuilder, root, "description", description);
            if (price != null && price >= 0L) {
                predicates.add(criteriaBuilder.equal(root.get("price"), price));
            }

            return predicates.isEmpty()
                    ? null
                    : criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });

        return productRepository
                .findAll(spec, pageable)
                .map(productMapper::toProductDTO);
    }

    private void addTextPredicate(List<Predicate> predicates, CriteriaBuilder cb, Root<ProductEntity> root, String field, String value) {
        if (StringUtils.hasText(value)) {
            predicates.add(cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%"));
        }
    }

    public void deleteProduct(int id) {
        if (!productRepository.existsById(id)) {
            throw new EmptyResultDataAccessException("Нет продукта с id: " + id, 1);
        }
        productRepository.deleteById(id);
    }
}
