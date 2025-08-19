package com.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder(builderMethodName = "productDtoBuilder")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductShortDTO {
    int id;

    @NotBlank(message = "Название обязательно")
    @Pattern(
            regexp = "[A-Za-zА-Яа-яЁё0-9\\s]+",
            message = "Некорректное имя"
    )
    String name;

    @NotNull(message = "Цена обязательно")
    @DecimalMin(value = "0.0", message = "Цена должна быть положительной")
    BigDecimal price;
}
