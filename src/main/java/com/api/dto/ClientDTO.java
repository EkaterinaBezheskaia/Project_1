package com.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder(builderMethodName = "clientDtoBuilder")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientDTO {

    int id;

    @NotBlank(message = "Имя обязательно")
    String name;

    @NotBlank(message = "Фамилия обязательна")
    String surname;

    @NotBlank(message = "Email обязательно")
    @Email
    String emailAddress;

    @NotBlank(message = "Номер телефона обязательно")
    String phoneNumber;

    List<OrderDTO> orders = new ArrayList<>();
}
