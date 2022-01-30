package kz.sabyrzhan.repositories;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import kz.sabyrzhan.entities.OrderItemEntity;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrderItemRepository implements PanacheRepositoryBase<OrderItemEntity, Integer> {

}
