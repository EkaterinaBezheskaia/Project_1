package com.store.entities;

import jakarta.persistence.*;
import lombok.*;

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
    @Column(name = "products_id", nullable = false)
    private int id;

    @Column(name = "products_name", unique = true, nullable = false)
    private String name;

    @Column(name = "products_description", nullable = false)
    private String description;

    @Column(name = "products_price", nullable = false)
    private Long price;
}