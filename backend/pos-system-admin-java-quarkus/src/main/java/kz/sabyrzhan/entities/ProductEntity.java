package kz.sabyrzhan.entities;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "pos_products")
@NamedQuery(
        name = "ProductWithCount.topProducts",
        query = "select new kz.sabyrzhan.model.ProductWithCount(o.productId, count(o.productId) as orderCount) " +
                "from OrderItemEntity o " +
                "left join OrderEntity e on e.id = o.orderId " +
                "where o.created > :date and e.status = :status " +
                "group by o.productId order by orderCount desc"
)
@Getter
@Setter
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

    @Column
    private Instant created = Instant.now();

    public void decreaseStockAmount(int purchasedQuantity) {
        setStock(getStock() - purchasedQuantity);
    }
}
