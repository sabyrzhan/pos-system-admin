package kz.sabyrzhan.services;

import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.entities.ProductEntity;
import kz.sabyrzhan.repositories.ProductRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ProductService {
    @Inject
    ProductRepository productRepository;

    public Uni<ProductEntity> getProduct(int id) {
        return productRepository.findById(id);
    }

    public Uni<ProductEntity> addProduct(ProductEntity entity) {
        return productRepository.findByName(entity.getName()).onItem().transformToUni(v -> productRepository.persist(entity));
    }
}