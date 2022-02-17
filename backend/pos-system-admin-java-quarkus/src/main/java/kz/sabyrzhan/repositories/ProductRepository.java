package kz.sabyrzhan.repositories;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.entities.ProductEntity;
import kz.sabyrzhan.exceptions.EntityNotFoundException;
import kz.sabyrzhan.model.OrderStatus;
import kz.sabyrzhan.model.ProductWithCount;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ApplicationScoped
public class ProductRepository implements PanacheRepositoryBase<ProductEntity, Integer> {
    public static final String PRODUCT_NOT_FOUND_MESSAGE = "Product not found";

    public Uni<ProductEntity> findById(int id) {
        return ProductEntity.<ProductEntity>findById(id).onItem().transformToUni(c -> {
            if (c == null) {
                return Uni.createFrom().failure(new EntityNotFoundException(PRODUCT_NOT_FOUND_MESSAGE));
            }
            return Uni.createFrom().item(c);
        });
    }

    public Uni<ProductEntity> findByName(String name) {
        return ProductEntity.<ProductEntity>find("name = ?1", name).list()
                .onItem().transformToUni(items -> {
                    if (items.size() == 0) {
                        return Uni.createFrom().failure(new EntityNotFoundException(PRODUCT_NOT_FOUND_MESSAGE));
                    }

                    return Uni.createFrom().item(items.get(0));
                });
    }

    public Uni<ProductEntity> persist(ProductEntity entity) {
        return Panache.withTransaction(entity::persist);
    }

    public Uni<List<ProductEntity>> persistAll(List<ProductEntity> updatedEntities) {
        List<Uni<ProductEntity>> unis = new ArrayList<>();
        for(ProductEntity entity : updatedEntities) {
            unis.add(Panache.withTransaction(entity::persist));
        }

        return Uni.combine().all().unis(unis).combinedWith(products -> (List<ProductEntity>) products);
    }

    public Multi<ProductEntity> findProducts(int page, int sizePerPage) {
        return find("order by created desc")
                .page(page - 1, sizePerPage)
                .stream();
    }

    public Uni<List<ProductEntity>> list(Set<Integer> productIds) {
        return list("id in (?1)", productIds);
    }

    public Uni<Void> returnQuantities(Map<Integer, Integer> productIdToQuantityMap) {
        List<Uni<Integer>> updates = new ArrayList<>();
        for(Map.Entry<Integer, Integer> entry : productIdToQuantityMap.entrySet()) {
            updates.add(update("stock = stock + ?1 where id = ?2", entry.getValue(), entry.getKey()));
        }

        return Uni.combine().all().unis(updates).combinedWith(v -> null);
    }

    public Uni<Long> countAvailableProducts() {
        return count("stock > 0");
    }

    public Uni<List<ProductWithCount>> countTopOrderedProducts() {
        var date = LocalDateTime.now().minusMonths(6).toInstant(ZoneOffset.UTC);
        return find("#ProductWithCount.topProducts", Parameters.with("date", date).and("status", OrderStatus.DONE))
                .page(0, 5)
                .list();
    }
}
