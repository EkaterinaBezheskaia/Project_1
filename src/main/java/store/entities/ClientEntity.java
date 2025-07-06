package store.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "clients")
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "0_id")
    private long id;

    @Column(name = "1_name")
    private String name;

    @Column(name = "2_surname")
    private String surname;

    @Column(name = "3_email_address", unique = true)
    private String emailAddress;

    @Column(name = "phone_number")
    private String phoneNumber;

}
