package kz.sabyrzhan.repositories;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.entities.OrderItemEntity;
import kz.sabyrzhan.entities.ProductEntity;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class OrderItemRepository implements PanacheRepositoryBase<OrderItemEntity, Integer> {
    public Uni<List<OrderItemEntity>> persistAll(List<OrderItemEntity> items) {
        List<Uni<ProductEntity>> unis = new ArrayList<>();
        for(OrderItemEntity entity : items) {
            unis.add(Panache.withTransaction(entity::persist));
        }

        return Uni.combine().all().unis(unis).combinedWith(savedItems -> (List<OrderItemEntity>) savedItems);
    }
}
