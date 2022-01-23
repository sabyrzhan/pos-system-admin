package kz.sabyrzhan.entities;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import kz.sabyrzhan.model.PaymentType;
import lombok.Data;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "pos_invoices")
@Data
public class InvoiceEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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
    @Enumerated
    private PaymentType paymentType;

    @Column
    private ZonedDateTime created;
}
