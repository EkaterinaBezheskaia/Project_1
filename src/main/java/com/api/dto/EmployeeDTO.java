package com.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import com.store.entities.Position;

@Data
@Builder(builderMethodName = "employeeDtoBuilder")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeDTO {
    int id;

    @NotBlank(message = "Имя обязательно")
    String name;

    @NotBlank(message = "Фамилия обязательна")
    String surname;

    @NotBlank(message = "Email обязательно")
    @Email
    String emailAddress;

    @NotBlank(message = "Пароль обязательно")
    String password;

    @NotBlank(message = "Позиция обязательно")
    Position position;
}
