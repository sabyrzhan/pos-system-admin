package kz.sabyrzhan.entities;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import kz.sabyrzhan.model.PaymentType;
import lombok.Data;

import javax.persistence.*;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pos_orders")
@Data
public class OrderEntity extends PanacheEntityBase {
    @Id
    private String id = UUID.randomUUID().toString();

    @Column(name = "customer_name")
    private String customerName;

    @Column
    private float subtotal;

    @Column
    private float tax;

    @Column
    private float discount;

    @Column
    private float total;

    @Column
    private float paid;

    @Column
    private float due;

    @Column(name = "payment_type")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column
    private Instant created = Instant.now();

    @Transient
    private List<OrderItemEntity> items = new ArrayList<>();
}
