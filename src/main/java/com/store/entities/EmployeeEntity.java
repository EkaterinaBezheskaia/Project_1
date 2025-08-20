package com.store.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "email_address", unique = true, nullable = false)
    @Email
    private String emailAddress;

    @Column(name = "password", nullable = false)
    @Length(min = 6, message = "Пароль не менее 6 символов")
    private String password;

    @Column(name = "position", nullable = false)
    @Enumerated(EnumType.STRING)
    private Position position;

}

