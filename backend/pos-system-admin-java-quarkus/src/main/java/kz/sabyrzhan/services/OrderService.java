package kz.sabyrzhan.services;

import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.entities.OrderEntity;
import kz.sabyrzhan.entities.OrderItemEntity;
import kz.sabyrzhan.entities.ProductEntity;
import kz.sabyrzhan.entities.StoreConfigEntity;
import kz.sabyrzhan.exceptions.EntityNotFoundException;
import kz.sabyrzhan.exceptions.InvalidOrderItemsException;
import kz.sabyrzhan.model.ConfigKey;
import kz.sabyrzhan.repositories.OrderItemRepository;
import kz.sabyrzhan.repositories.OrderRepository;
import kz.sabyrzhan.repositories.ProductRepository;
import kz.sabyrzhan.repositories.StoreConfigRepository;
import kz.sabyrzhan.services.dto.TransientHolder;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationScoped
@Slf4j
public class OrderService {
    @Inject
    ProductRepository productRepository;

    @Inject
    StoreConfigRepository storeConfigRepository;

    @Inject
    OrderItemRepository orderItemRepository;

    @Inject
    OrderRepository orderRepository;

    public Uni<OrderEntity> createOrder(final OrderEntity requestOrder) {
        final TransientHolder holder = new TransientHolder();
        return Uni.createFrom().nullItem()
                .onItem().transformToUni(v -> {
                    Set<Integer> productIds = new HashSet<>();
                    Map<Integer, OrderItemEntity> itemsWithoutDuplicates = new HashMap<>();
                    Optional.ofNullable(requestOrder.getItems()).ifPresent(items -> items.forEach(item -> {
                        productIds.add(item.getProductId());
                        Optional.ofNullable(itemsWithoutDuplicates.get(item.getProductId()))
                                .ifPresentOrElse(existingItem -> existingItem.addQuantity(item.getQuantity()),
                                                () -> itemsWithoutDuplicates.put(item.getProductId(), item.copy()));
                    }));
                    requestOrder.setItems(itemsWithoutDuplicates.values().stream().toList());

                    if (productIds.isEmpty()) {
                        return Uni.createFrom().failure(new InvalidOrderItemsException("Order items not specified"));
                    }

                    return Uni.createFrom().item(productIds);
                })
                .onItem().transformToUni(productIds -> productRepository.list(productIds).onItem().transformToUni(products -> {
                    if (products.size() != productIds.size()) {
                        return Uni.createFrom().failure(new InvalidOrderItemsException("Invalid products specified"));
                    }
                    return Uni.createFrom().item(products);
                }))
                .onItem().transformToUni(products -> {
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
                        orderItem.setCreated(requestOrder.getCreated());
                        orderItem.setPrice(product.getSalePrice());
                        orderItems.add(orderItem);

                        product.decreaseStockAmount(orderItem.getQuantity());
                        updatedProducts.add(product);
                    }
                    requestOrder.setItems(orderItems);

                    holder.setProductsEntities(updatedProducts);
                    return Uni.createFrom().nullItem();
                })
                .onItem().transformToUni(v -> storeConfigRepository.findByConfigKey(ConfigKey.TAX_PERCENT))
                .onItem().transform(taxConfig -> {
                    holder.putConfig(ConfigKey.TAX_PERCENT, taxConfig);
                    return null;
                })
                .onItem().transformToUni(v -> {
                    try {
                        validateOrderEntity(requestOrder, holder);
                        return Uni.createFrom().nullItem();
                    } catch (IllegalArgumentException e) {
                        return Uni.createFrom().failure(e);
                    }
                })
                .onItem().transformToUni(v -> {
                    List<ProductEntity> products = holder.getProductsEntities();
                    return productRepository.persistAll(products).onItem().transform(savedProducts -> {
                        holder.setProductsEntities(savedProducts);
                        return null;
                    });
                })
                .onItem().transformToUni(v -> orderRepository.persist(requestOrder))
                .onItem().transformToUni(savedOrderEntity -> {
                    savedOrderEntity.getItems().forEach(item -> item.setOrderId(savedOrderEntity.getId()));
                    holder.setOrderEntity(savedOrderEntity);
                    return orderItemRepository.persistAll(requestOrder.getItems());
                })
                .onItem().transformToUni(savedItems -> {
                    var savedOrderEntity = holder.getOrderEntity();
                    savedOrderEntity.setItems(savedItems);
                    return Uni.createFrom().item(savedOrderEntity);
                });
    }

    public void validateOrderEntity(OrderEntity requestEntity, TransientHolder holder) {
        Map<Integer, ProductEntity> productMap = holder.getProductsAsMap();
        var taxConfig = holder.getConfig(ConfigKey.TAX_PERCENT);

        float subtotal = 0;
        float total = 0;
        float tax = 0;
        float discount = requestEntity.getDiscount();
        float paid = requestEntity.getPaid();
        float due = 0;

        for(OrderItemEntity orderItem : requestEntity.getItems()) {
            subtotal += productMap.get(orderItem.getProductId()).getSalePrice() * orderItem.getQuantity();
        }

        tax = subtotal * (Float.valueOf(taxConfig.getConfigValue()) / 100);
        total = subtotal + tax - discount;
        due = paid - total;

        if (subtotal != requestEntity.getSubtotal()) {
            log.info("Invalid subtotal: expected='{}', actual='{}'", subtotal, requestEntity.getSubtotal());
            throw new IllegalArgumentException("Subtotal is invalid");
        }

        if (tax != requestEntity.getTax()) {
            log.info("Invalid tax: expected='{}', actual='{}'", tax, requestEntity.getTax());
            throw new IllegalArgumentException("Tax is invalid");
        }

        if (total != requestEntity.getTotal()) {
            log.info("Invalid total: expected='{}', actual='{}'", total, requestEntity.getTotal());
            throw new IllegalArgumentException("Total is invalid");
        }

        if (due != requestEntity.getDue()) {
            log.info("Invalid due: expected='{}', actual='{}'", due, requestEntity.getDue());
            throw new IllegalArgumentException("Due is invalid");
        }
    }

    public Uni<OrderEntity> cancelOrder(String id) {
        var holder = new TransientHolder();
        return orderRepository.cancelOrder(id)
                .onItem().transform(canceledOrder -> {
                    holder.setOrderEntity(canceledOrder);
                    return null;
                })
                .onItem().transformToUni(v -> orderItemRepository.findByOrderId(holder.getOrderEntity().getId()))
                .onItem().transform(items -> {
                    holder.getOrderEntity().setItems(items);
                    return null;
                })
                .onItem().transformToUni(v -> {
                    var productIdToQuantityMap = holder.getOrderEntity().getItems().stream()
                            .collect(Collectors.toMap(OrderItemEntity::getProductId, OrderItemEntity::getQuantity));
                    return productRepository.returnQuantities(productIdToQuantityMap);
                })
                .onItem().transform(v -> holder.getOrderEntity());
    }
}
