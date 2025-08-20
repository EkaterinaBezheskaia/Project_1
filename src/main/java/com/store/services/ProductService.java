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

    public ProductDTO updateProduct(int id, Map<String, String> updates) {

        ProductEntity productEntity = productRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не найден"));

        updates.forEach((key, value) -> {
            if (key == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ключ не может быть null");
            if (value == null || value.trim().isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Пустое значение для " + key);
            value = value.trim();
            switch (key) {
                case "name" -> productEntity.setName(value);
                case "description" -> productEntity.setDescription(value);
                case "price" -> {
                        BigDecimal price = new BigDecimal(value);
                        productEntity.setPrice(price);
                }
                default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неизвестное поле: " + key);
            }
        });
        Optional<ProductEntity> sameProduct = productRepository.findByNameAndDescription(productEntity.getName(), productEntity.getDescription());
        if (sameProduct.isPresent() && sameProduct.get().getId() != productEntity.getId()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Продукт с таким названием и описанием уже существует");
        }

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
