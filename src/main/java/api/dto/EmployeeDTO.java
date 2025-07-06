package api.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import store.entities.Position;

@Data
@Builder(builderMethodName = "fileDtoBuilder")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeDTO {
    long id;
    String name;
    String surname;
    String emailAddress;
    String password;
    Position position;
}
