package com.api.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import com.store.entities.Position;

@Data
@Builder(builderMethodName = "employeeDtoBuilder")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeDTO {
    int id;
    String name;
    String surname;
    String emailAddress;
    String password;
    Position position;
}
