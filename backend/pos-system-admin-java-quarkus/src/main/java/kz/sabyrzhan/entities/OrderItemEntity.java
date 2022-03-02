package kz.sabyrzhan.entities;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "pos_order_items")
@Getter
@Setter
public class OrderItemEntity extends PanacheEntityBase implements Cloneable {
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

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    @Override
    public OrderItemEntity clone() {
        OrderItemEntity clone = new OrderItemEntity();
        clone.setId(id);
        clone.setOrderId(orderId);
        clone.setProductId(productId);
        clone.setProductName(productName);
        clone.setQuantity(quantity);
        clone.setPrice(price);
        clone.setCreated(created);

        return clone;
    }
}