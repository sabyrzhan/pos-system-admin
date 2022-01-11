package kz.sabyrzhan.entities;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "pos_categories")
@Data
public class CategoryEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;
}
