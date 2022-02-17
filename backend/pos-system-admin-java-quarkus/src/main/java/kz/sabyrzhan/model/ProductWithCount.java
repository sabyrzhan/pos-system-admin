package kz.sabyrzhan.model;

import kz.sabyrzhan.entities.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductWithCount extends ProductEntity {
    private int id;
    private long orderCount;
}
