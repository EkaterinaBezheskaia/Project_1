package api.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder(builderMethodName = "fileDtoBuilder")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDTO {
    long id;
    Instant createdAt;
    String status;
}
