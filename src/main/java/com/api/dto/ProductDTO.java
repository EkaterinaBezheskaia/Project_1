package com.api.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder(builderMethodName = "productDtoBuilder")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDTO {
    int id;
    String name;
    String description;
    Long price;
}
