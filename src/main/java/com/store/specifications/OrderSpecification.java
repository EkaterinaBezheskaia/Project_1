package com.store.specifications;

import com.store.entities.OrderEntity;
import com.store.entities.Status;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

@UtilityClass
public class OrderSpecification {
    public static Specification<OrderEntity> dateEquals(LocalDate date) {
        return (root, query, cb) -> {
            if (date == null) return null;
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.plusDays(1).atStartOfDay();
            return cb.between(root.get("creationDate"), start, end);
        };
    }

    public static Specification<OrderEntity> statusEquals(Status status) {
        return (root, query, cb) -> {
            if (status == null) return null;
            return cb.equal(root.get("status"), status);
        };
    }
}
