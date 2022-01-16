package kz.sabyrzhan.model;

import kz.sabyrzhan.entities.CategoryEntity;
import kz.sabyrzhan.entities.ProductEntity;
import lombok.Data;

import java.time.Instant;

@Data
public class Product {
    private int id;
    private String name;
    private String category;
    private float purchasePrice;
    private float salePrice;
    private int stock;
    private String description;
    private String images;
    private Instant created;

    public static Product fromEntity(ProductEntity entity, CategoryEntity categoryEntity) {
        var result = new Product();
        result.setId(entity.getId());
        result.setName(entity.getName());
        result.setCategory(categoryEntity.getName());
        result.setPurchasePrice(entity.getPurchasePrice());
        result.setSalePrice(entity.getSalePrice());
        result.setStock(entity.getStock());
        result.setDescription(entity.getDescription());
        result.setImages(entity.getImages());
        result.setCreated(entity.getCreated());

        return result;
    }
}