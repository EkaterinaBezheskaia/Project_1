package store.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employees")
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "0_id")
    private int id;

    @Column(name = "1_name")
    private String name;

    @Column(name = "2_surname")
    private String surname;

    @Column(name = "3_email_address", unique = true)
    private String emailAddress;

    @Column(name = "password")
    @Length(min = 6)
    private String password;

    @Column(name = "position")
    private String position;

}
