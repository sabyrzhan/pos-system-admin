package kz.sabyrzhan.entities;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import kz.sabyrzhan.model.OrderStatus;
import kz.sabyrzhan.model.PaymentType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pos_orders")
@Getter
@Setter
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

    @Column
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.NEW;

    @Transient
    private List<OrderItemEntity> items = new ArrayList<>();
}
