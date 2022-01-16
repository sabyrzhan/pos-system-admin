package kz.sabyrzhan.repositories;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.entities.ProductEntity;
import kz.sabyrzhan.exceptions.EntityAlreadyExistsException;
import kz.sabyrzhan.exceptions.EntityNotFoundException;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductRepository implements PanacheRepositoryBase<ProductEntity, Integer> {
    public static final String PRODUCT_NOT_FOUND_MESSAGE = "Product not found";
    public static final String PRODUCT_NAME_ALREADY_EXISTS = "Product with this name already exists";

    public Uni<ProductEntity> findById(int id) {
        return ProductEntity.<ProductEntity>findById(id).onItem().transformToUni(c -> {
            if (c == null) {
                return Uni.createFrom().failure(new EntityNotFoundException(PRODUCT_NOT_FOUND_MESSAGE));
            }
            return Uni.createFrom().item(c);
        });
    }

    public Uni<ProductEntity> findByName(String name) {
        return ProductEntity.<ProductEntity>find("name = ?1", name).count()
                .onItem().transformToUni(c -> {
                    if (c != 0) {
                        return Uni.createFrom().failure(new EntityAlreadyExistsException(PRODUCT_NAME_ALREADY_EXISTS));
                    }

                    return Uni.createFrom().nullItem();
                });
    }

    public Uni<ProductEntity> persist(ProductEntity entity) {
        return Panache.withTransaction(entity::persist);
    }

    public Multi<ProductEntity> findProducts(int page, int sizePerPage) {
        return find("order by created desc")
                .page(page - 1, sizePerPage)
                .stream();
    }
}
