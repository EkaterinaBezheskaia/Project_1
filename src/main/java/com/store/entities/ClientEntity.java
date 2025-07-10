package com.store.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "clients")
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clients_id", nullable = false)
    @Setter(AccessLevel.NONE)
    private int id;

    @Column(name = "clients_name", nullable = false)
    private String name;

    @Column(name = "clients_surname", nullable = false)
    private String surname;

    @Column(name = "clients_email_address", unique = true, nullable = false)
    private String emailAddress;

    @Column(name = "clients_phone_number", unique = true, nullable = false)
    private String phoneNumber;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderEntity> orders = new ArrayList<>();
}