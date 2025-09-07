package com.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import com.store.entities.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder(builderMethodName = "orderDtoBuilder")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    int id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    LocalDateTime creationDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    LocalDateTime updatedDate;

    @NotNull(message = "Статус обязательно: " +
            "NEW, PROCESSING, COMPLETED, CANCELED")
    Status status;

    @Builder.Default
    List<ProductShortDTO> productsList = new ArrayList<>();

    @NotNull(message = "Клиент обязателен")
    int clientId;
}
