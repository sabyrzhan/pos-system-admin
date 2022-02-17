package kz.sabyrzhan.repositories;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.entities.OrderEntity;
import kz.sabyrzhan.exceptions.EntityNotFoundException;
import kz.sabyrzhan.exceptions.InvalidStatusException;
import kz.sabyrzhan.model.OrderStatus;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrderRepository implements PanacheRepositoryBase<OrderEntity, Integer> {
    public Multi<OrderEntity> list(int page, int sizePerPage) {
        return OrderEntity.findAll(Sort.descending("created"))
                .page(page - 1, sizePerPage).stream();
    }

    public Uni<OrderEntity> cancelOrder(String orderId) {
        return find("id = ?1", orderId).page(0,1).list()
                .onItem().transformToUni(order -> {
                    if (order.isEmpty()) {
                        return Uni.createFrom().failure(new EntityNotFoundException("Order not found"));
                    }
                    if (order.get(0).getStatus() == OrderStatus.PROCESSING) {
                        return Uni.createFrom().failure(new InvalidStatusException("Cannot cancel processing order"));
                    }
                    var orderEntity = order.get(0);
                    orderEntity.setStatus(OrderStatus.CANCELLED);
                    return Panache.withTransaction(orderEntity::persist);
                });
    }

    public Uni<OrderEntity> findById(String id) {
        return find("id = ?1", id).page(0,1).list()
                .onItem().transformToUni(list -> {
                    if (list.isEmpty()) {
                        return Uni.createFrom().failure(new EntityNotFoundException("Order not found"));
                    }
                    
                    return Uni.createFrom().item(list.get(0));
                });
    }

    public Uni<Long> countOrders(OrderStatus status) {
        return count("status = ?1", status);
    }

    public Uni<Void> updateStatus(String id, OrderStatus status) {
        return update("status = ?1 where id = ?2", status, id).onItem().transformToUni(v -> Uni.createFrom().voidItem());
    }
}
