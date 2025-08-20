package com.store.specifications;

import com.store.entities.ProductEntity;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

@UtilityClass
public class ProductSpecification {
    public static Specification<ProductEntity> nameContains(String name) {
        return (root, query, cb) -> (name == null || name.isBlank())
                ? null
                :cb.like(root.get("name"), "%" + name.toLowerCase() + "%");
    }
    public static Specification<ProductEntity> descriptionContains(String description) {
        return (root, query, cb) -> (description == null || description.isBlank())
                ? null
                :cb.like(root.get("description"), "%" + description.toLowerCase() + "%");
    }
    public static Specification<ProductEntity> priceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, cb) -> {
            if (minPrice == null && maxPrice == null) return null;
            if (minPrice != null && maxPrice != null)
                return cb.between(root.get("price"), minPrice, maxPrice);
            if (minPrice != null)
                return cb.greaterThanOrEqualTo(root.get("price"), minPrice);
            return cb.lessThanOrEqualTo(root.get("price"), maxPrice);
        };
    }
}
