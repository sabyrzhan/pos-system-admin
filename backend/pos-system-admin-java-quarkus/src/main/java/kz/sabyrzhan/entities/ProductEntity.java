package kz.sabyrzhan.entities;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "pos_products")
@Data
public class ProductEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Column(name = "category_id")
    private int categoryId;

    @Column(name = "purchase_price")
    private float purchasePrice;

    @Column(name = "sale_price")
    private float salePrice;

    @Column
    private int stock;

    @Column
    private String description;

    @Column
    private String images;
}
