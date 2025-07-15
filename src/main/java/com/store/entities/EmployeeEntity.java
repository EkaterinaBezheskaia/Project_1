package com.store.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employees")
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employees_id", nullable = false)
    private int id;

    @Column(name = "employees_name", nullable = false)
    private String name;

    @Column(name = "employees_surname", nullable = false)
    private String surname;

    @Column(name = "employees_email_address", unique = true, nullable = false)
    private String emailAddress;

    @Column(name = "employees_password", nullable = false)
    @Length(min = 6)
    private String password;

    @Column(name = "employees_position", nullable = false)
    @Enumerated(EnumType.STRING)
    private Position position;

}

