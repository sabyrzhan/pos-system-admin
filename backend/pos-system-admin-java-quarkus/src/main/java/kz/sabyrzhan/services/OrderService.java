package kz.sabyrzhan.services;

import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.entities.OrderItemEntity;
import kz.sabyrzhan.entities.OrderEntity;
import kz.sabyrzhan.entities.ProductEntity;
import kz.sabyrzhan.exceptions.InvalidOrderItemsException;
import kz.sabyrzhan.repositories.ProductRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class OrderService {
    @Inject
    ProductRepository productRepository;

    public Uni<Void> createOrder(final OrderEntity order) {
        return Uni.createFrom().item(order.getItems())
                .onItem().transformToUni(items -> {
                    Map<Integer, Integer> productToQuantityMap = new HashMap<>();
                    if (order.getItems() != null) {
                        for(OrderItemEntity item : order.getItems()) {
                            productToQuantityMap.putIfAbsent(item.getProductId(), 0);
                            productToQuantityMap.computeIfPresent(item.getProductId(), (key, oldValue) -> productToQuantityMap.get(key));
                        }
                    }

                    if (productToQuantityMap.isEmpty()) {
                        return Uni.createFrom().failure(new InvalidOrderItemsException("Order items not specified"));
                    }

                    return Uni.createFrom().item(productToQuantityMap);
                })
                .onItem().transformToUni(productToQuantityMap -> {
                    Uni<List<ProductEntity>> productsUni = productRepository.list(productToQuantityMap.keySet());
                    return productsUni.onItem().transformToUni(products -> {
                        if (products.size() != productToQuantityMap.size()) {
                            return Uni.createFrom().failure(new InvalidOrderItemsException("Invalid products specified"));
                        }

                        return Uni.createFrom().nullItem();
                    });
                });
    }
}
