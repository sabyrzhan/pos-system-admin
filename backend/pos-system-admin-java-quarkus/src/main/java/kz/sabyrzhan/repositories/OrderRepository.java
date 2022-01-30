package kz.sabyrzhan.repositories;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import kz.sabyrzhan.entities.OrderEntity;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrderRepository implements PanacheRepositoryBase<OrderEntity, Integer> {
}
