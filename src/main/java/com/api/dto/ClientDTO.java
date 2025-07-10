package com.api.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder(builderMethodName = "clientDtoBuilder")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientDTO {
    int id;
    String name;
    String surname;
    String emailAddress;
    String phoneNumber;
    List<OrderDTO> orders = new ArrayList<>();
}
