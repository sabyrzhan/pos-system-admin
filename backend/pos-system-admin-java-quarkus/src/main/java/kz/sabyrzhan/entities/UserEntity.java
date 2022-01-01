package kz.sabyrzhan.entities;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "pos_users")
@Data
public class UserEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String username;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String salt;

    @Column
    private String role;
}
