package kz.sabyrzhan.entities;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import lombok.Data;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "pos_invoice_details")
@Data
public class InvoiceDetailsEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "invoice_id")
    private int invoiceId;

    @Column(name = "product_id")
    private int productId;

    @Column(name = "product_name")
    private String productName;

    @Column
    private int quantity;

    @Column
    private float price;

    @Column
    private ZonedDateTime created;
}