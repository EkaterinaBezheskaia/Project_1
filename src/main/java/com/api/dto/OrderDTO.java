package com.api.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import com.store.entities.Status;

import java.time.Instant;
import java.util.List;

@Data
@Builder(builderMethodName = "fileDtoBuilder")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDTO {
    long id;
    Instant createdAt;
    Status status;
    List<ProductDTO> products;
    ClientDTO client;
}
