package com.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder(builderMethodName = "clientDtoBuilder")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientDTO {

    int id;

    @NotBlank(message = "Имя обязательно")
    @Pattern(
            regexp = "[A-Za-zА-Яа-яЁё\\s]+",
            message = "Некорректное имя"
    )
    String name;

    @NotBlank(message = "Фамилия обязательна")
    @Pattern(
            regexp = "[A-Za-zА-Яа-яЁё\\s]+",
            message = "Некорректная фамилия"
    )
    String surname;

    @NotBlank(message = "Email обязательно")
    @Email(message = "Некорректный email-адрес")
    String emailAddress;

    @NotBlank(message = "Номер телефона обязательно")
    @Pattern(
            regexp = "^\\+7\\d{10}$",
            message = "Некорректный номер телефона. Формат: +7XXXXXXXXXX"
    )
    String phoneNumber;

    List<OrderDTO> orders;
}
