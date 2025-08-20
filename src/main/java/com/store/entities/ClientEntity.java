package com.store.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @Column(name = "id", nullable = false)
    @Setter(AccessLevel.NONE)
    private int id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Имя обязательно")
    @Pattern(
            regexp = "[A-Za-zА-Яа-яЁё\\s]+",
            message = "Некорректное имя"
    )
    private String name;

    @Column(name = "surname", nullable = false)
    @NotBlank(message = "Фамилия обязательна")
    @Pattern(
            regexp = "[A-Za-zА-Яа-яЁё\\s]+",
            message = "Некорректная фамилия"
    )
    private String surname;

    @Column(name = "email", unique = true, nullable = false)
    @NotBlank(message = "Email обязательно")
    @Email(message = "Некорректный email-адрес")
    private String emailAddress;

    @Column(name = "phone", unique = true, nullable = false)
    @NotBlank(message = "Номер телефона обязательно")
    @Pattern(
            regexp = "^\\+7\\d{10}$",
            message = "Некорректный номер телефона. Формат: +7XXXXXXXXXX"
    )
    private String phoneNumber;

    @Builder.Default
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderEntity> orders = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientEntity that = (ClientEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}