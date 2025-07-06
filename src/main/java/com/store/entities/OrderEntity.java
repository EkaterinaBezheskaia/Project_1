package com.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_id", nullable = false)
    private long id;

    @Column(name = "orders_creationDate", nullable = false)
    Instant createdAt = Instant.now();

    @Column(name = "orders_status", nullable = false)
    private Status status;

    @Column(name = "orders_products")
    @ManyToMany(mappedBy = "products_id", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<ProductEntity> products;

    @JoinColumn(name = "client", referencedColumnName = "clients_orders", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ClientEntity client;
}
