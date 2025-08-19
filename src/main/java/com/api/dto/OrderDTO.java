package com.api.dto;

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
    int id;
    LocalDateTime creationDate;

    @NotNull(message = "Статус обязательно: " +
            "NEW, PROCESSING, COMPLETED, CANCELED")
    Status status;

    //TO DO: Необходимо, чтобы во время вызова GET на клиента выводился список с продуктами в заказах
    List<ProductShortDTO> productsList = new ArrayList<>();

    @NotNull(message = "Клиент обязателен")
    int clientId;
}
