package com.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;
import com.store.entities.Position;
import org.hibernate.validator.constraints.Length;

@Data
@Builder(builderMethodName = "employeeDtoBuilder")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeDTO {
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

    @NotBlank(message = "Пароль обязательно")
    @Length(min = 6, message = "Пароль не менее 6 символов")
    String password;

    @NotNull(message = "Позиция обязательно: MANAGER или ADMINISTRATOR")
    Position position;
}
