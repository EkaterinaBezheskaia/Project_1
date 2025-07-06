package store.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "0_id")
    private long id;

    @Column(name = "1_name", unique = true)
    private String name;

    @Column(name = "2_description")
    private String description;

    @Column(name = "3_price")
    private long price;

}
