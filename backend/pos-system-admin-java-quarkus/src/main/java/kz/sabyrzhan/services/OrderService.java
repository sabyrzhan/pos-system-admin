package kz.sabyrzhan.services;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import kz.sabyrzhan.entities.OrderItemEntity;
import kz.sabyrzhan.entities.OrderEntity;
import kz.sabyrzhan.entities.ProductEntity;
import kz.sabyrzhan.entities.StoreConfigEntity;
import kz.sabyrzhan.exceptions.EntityNotFoundException;
import kz.sabyrzhan.exceptions.InvalidOrderItemsException;
import kz.sabyrzhan.model.ConfigKey;
import kz.sabyrzhan.repositories.OrderItemRepository;
import kz.sabyrzhan.repositories.OrderRepository;
import kz.sabyrzhan.repositories.ProductRepository;
import kz.sabyrzhan.repositories.StoreConfigRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationScoped
public class OrderService {
    @Inject
    ProductRepository productRepository;

    @Inject
    StoreConfigRepository storeConfigRepository;

    @Inject
    OrderItemRepository orderItemRepository;

    @Inject
    OrderRepository orderRepository;

    public Uni<Void> createOrder(final OrderEntity requestOrder) {
        return Uni.createFrom().item(requestOrder.getItems())
                .onItem().transformToUni(items -> {
                    Map<Integer, Integer> productToQuantityMap = new HashMap<>();
                    if (requestOrder.getItems() != null) {
                        for(OrderItemEntity item : requestOrder.getItems()) {
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

                        Map<Integer, ProductEntity> productsMap = products.stream().collect(Collectors.toMap(ProductEntity::getId, Function.identity()));

                        List<OrderItemEntity> orderItems = new ArrayList<>();
                        List<ProductEntity> updatedProducts = new ArrayList<>();

                        ProductEntity product;
                        for(OrderItemEntity orderItem: requestOrder.getItems()) {
                            product = productsMap.get(orderItem.getProductId());
                            if (product == null) {
                                return Uni.createFrom().failure(new EntityNotFoundException("Product with id=" + orderItem.getProductId() + " not found."));
                            }
                            if (product.getStock() < orderItem.getQuantity()) {
                                return Uni.createFrom().failure(new InvalidOrderItemsException("Order item quantity is more than stock for productId=" + orderItem.getProductId()));
                            }
                            orderItem.setProductName(product.getName());
                            orderItem.setCreated(ZonedDateTime.now());
                            orderItem.setPrice(product.getSalePrice());
                            orderItems.add(orderItem);

                            product.decreaseStockAmount(orderItem.getQuantity());
                            updatedProducts.add(product);
                        }

                        return Uni.createFrom().item(Tuple2.of(orderItems, updatedProducts));
                    });
                })
                .onItem().transformToUni(items -> {
                    List<ProductEntity> products = items.getItem2();
                    return productRepository.persistAll(products).onItem().transform(v -> items.getItem1());
                })
                .onItem().transformToUni(orderItems -> storeConfigRepository.findByConfigKey(ConfigKey.TAX_PERCENT)
                                                        .onItem()
                                                        .transform(storeConfigEntity -> Tuple2.of(storeConfigEntity, orderItems)))
                .onItem().transformToUni(map -> {
                    StoreConfigEntity taxConfig = map.getItem1();
                    List<OrderItemEntity> items = map.getItem2();

                    requestOrder.setItems(items);
                    OrderEntity orderEntityToPersist = calculateAndCreateOrderEntity(requestOrder, taxConfig);

                    Uni<List<OrderItemEntity>> itemsUni = orderItemRepository.persistAll(orderEntityToPersist.getItems());
                    Uni<OrderEntity> orderUni = orderRepository.persist(orderEntityToPersist);

                    return Uni.combine().all().unis(orderUni, itemsUni).asTuple().onItem().transform(v -> null);
                });
    }

    private OrderEntity calculateAndCreateOrderEntity(OrderEntity requestEntity, StoreConfigEntity taxConfig) {
        return new OrderEntity();
    }
}
