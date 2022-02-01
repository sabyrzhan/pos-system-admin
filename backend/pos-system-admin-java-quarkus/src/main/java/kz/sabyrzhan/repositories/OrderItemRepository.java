package kz.sabyrzhan.repositories;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.entities.OrderItemEntity;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class OrderItemRepository implements PanacheRepositoryBase<OrderItemEntity, Integer> {
    public Uni<List<OrderItemEntity>> persistAll(List<OrderItemEntity> items) {
        return Uni.createFrom().item(items);
    }
}
