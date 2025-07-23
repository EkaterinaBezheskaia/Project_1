package com.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import com.store.entities.Status;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder(builderMethodName = "orderDtoBuilder")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDTO {
    int id;
    Instant createdAt;

    @NotBlank(message = "Статус обязательно")
    Status status;

    List<ProductDTO> products = new ArrayList<>();

    ClientDTO client;
}
