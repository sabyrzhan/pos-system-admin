package kz.sabyrzhan.services;

import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.entities.OrderDetailsEntity;
import kz.sabyrzhan.entities.OrderEntity;
import kz.sabyrzhan.exceptions.InvalidOrderItemsException;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class OrderService {
    public Uni<Void> createOrder(final OrderEntity order) {
        return Uni.createFrom().item(order.getItems())
                .onItem().transformToUni(items -> {
                    Map<Integer, Integer> productToQuantityMap = new HashMap<>();
                    if (order.getItems() != null) {
                        for(OrderDetailsEntity item : order.getItems()) {
                            productToQuantityMap.putIfAbsent(item.getProductId(), 0);
                            productToQuantityMap.computeIfPresent(item.getProductId(), (key, oldValue) -> productToQuantityMap.get(key));
                        }
                    }

                    if (productToQuantityMap.isEmpty()) {
                        return Uni.createFrom().failure(new InvalidOrderItemsException("Order order items specified"));
                    }

                    return Uni.createFrom().item(productToQuantityMap);
                })
                .onItem().transformToUni(productToQuantityMap -> {
                    return Uni.createFrom().nullItem();
                });
    }
}
