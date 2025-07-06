package api.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder(builderMethodName = "fileDtoBuilder")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientDTO {
    long id;
    String name;
    String surname;
    String emailAddress;
    String phoneNumber;
}
