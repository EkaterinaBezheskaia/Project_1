package com.store.services;

import com.api.dto.ProductDTO;
import com.api.mappers.ProductMapper;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.server.ResponseStatusException;

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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Название обязательно");
        }
        if (product.getDescription() == null || product.getDescription().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Описание обязательно");
        }
        if (product.getPrice() == null || product.getPrice() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Цена обязательна");
        }

        if (productRepository.existsByName(product.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Название должно быть уникальным");
        }

        if (!product.getName().matches("[A-Za-zА-Яа-я0-9]+")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректное название");
        }
        if (!product.getDescription().matches("[A-Za-zА-Яа-я0-9]+")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректное описание");
        }

        ProductEntity productEntity = productMapper.toProductEntity(product);
        return productMapper.toProductDTO(productRepository.save(productEntity));
    }

    public ProductDTO updateProduct(int id, String description) {

        ProductEntity productEntity = productRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не найден"));

        if (description == null || description.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Описание обязательно");
        }
        if (!description.matches("^(?!\\s+$)[\\p{L}0-9\\s,.!?;-]+$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Описание содержит недопустимые символы");
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет продукта с запрошенным id");
        }
        productRepository.deleteById(id);
    }
}
