package com.api.dto;

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
    long id;
    Instant createdAt;
    Status status;
    List<ProductDTO> products = new ArrayList<>();
    ClientDTO client;
}
