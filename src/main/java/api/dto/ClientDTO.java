package api.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

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
    List<OrderDTO> orders;
}
