package kz.sabyrzhan.entities;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "pos_order_items")
@Data
public class OrderItemEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "product_id")
    private int productId;

    @Column(name = "product_name")
    private String productName;

    @Column
    private int quantity;

    @Column
    private float price;

    @Column
    private Instant created;
}