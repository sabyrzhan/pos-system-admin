package kz.sabyrzhan.repositories;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Multi;
import kz.sabyrzhan.entities.OrderEntity;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrderRepository implements PanacheRepositoryBase<OrderEntity, Integer> {
    public Multi<OrderEntity> list(int page, int sizePerPage) {
        return OrderEntity.findAll(Sort.descending("created"))
                .page(page - 1, sizePerPage).stream();
    }
}
