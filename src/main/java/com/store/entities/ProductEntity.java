package com.store.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "products_id", nullable = false)
    private int id;

    @Column(name = "products_name", unique = true, nullable = false)
    @NotBlank(message = "Название обязательно")
    @Pattern(
            regexp = "^[\\p{L}0-9\\s.,!?;-]+$",
            message = "Некорректное название"
    )
    private String name;

    @Column(name = "products_description", nullable = false)
    @NotBlank(message = "Описание обязательно")
    @Pattern(
            regexp = "^^[\\p{L}0-9\\s.,!?;-]+$",
            message = "Некорректное описание"
    )
    private String description;

    @Column(name = "products_price", nullable = false)
    @NotNull(message = "Цена обязательно")
    @DecimalMin(value = "0.0", message = "Цена должна быть положительной")
    private BigDecimal price;

    @ManyToMany(mappedBy = "products")
    private List<OrderEntity> orders = new ArrayList<>();
}