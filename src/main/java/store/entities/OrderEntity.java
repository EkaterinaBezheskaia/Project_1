package store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "0_id")
    private long id;

    @Column(name = "1_creationDate")
    Instant createdAt = Instant.now();

    @Column(name = "2_status")
    private String status;

}
