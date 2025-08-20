package com.api.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder(builderMethodName = "productDtoBuilder")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDTO {
    int id;

    @NotBlank(message = "Название обязательно")
    @Pattern(
            regexp = "^[\\p{L}0-9\\s.,!?;-]+$",
            message = "Некорректное название"
    )
    String name;

    @NotBlank(message = "Описание обязательно")
    @Pattern(
            regexp = "^[\\p{L}0-9\\s.,!?;-]+$",
            message = "Некорректное описание"
    )
    String description;

    @NotNull(message = "Цена обязательно")
    @DecimalMin(value = "0.0", message = "Цена должна быть положительной")
    BigDecimal price;
}
