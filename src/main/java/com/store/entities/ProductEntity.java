package com.store.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name", unique = true, nullable = false)
    @NotBlank(message = "Название обязательно")
    @Pattern(
            regexp = "^[\\p{L}0-9\\s.,!?;-]+$",
            message = "Некорректное название"
    )
    private String name;

    @Column(name = "description", nullable = false)
    @NotBlank(message = "Описание обязательно")
    @Pattern(
            regexp = "^^[\\p{L}0-9\\s.,!?;-]+$",
            message = "Некорректное описание"
    )
    private String description;

    @Column(name = "price", nullable = false)
    @NotNull(message = "Цена обязательно")
    @DecimalMin(value = "0.0", message = "Цена должна быть положительной")
    private BigDecimal price;

    @ManyToMany(mappedBy = "products", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<OrderEntity> orders = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductEntity that = (ProductEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}