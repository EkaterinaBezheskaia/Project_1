package com.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder(builderMethodName = "productDtoBuilder")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDTO {
    int id;

    @NotBlank(message = "Название обязательно")
    String name;

    @NotBlank(message = "Описание обязательно")
    String description;

    @NotNull(message = "Цена обязательно")
    @Min(value = 0, message = "Цена должна быть положительной")
    Long price;
}
