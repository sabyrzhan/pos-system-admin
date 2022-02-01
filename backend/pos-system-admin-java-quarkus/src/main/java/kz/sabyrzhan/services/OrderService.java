package kz.sabyrzhan.services;

import io.smallrye.mutiny.Uni;
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
import kz.sabyrzhan.services.dto.TransientHolder;
import lombok.extern.slf4j.Slf4j;

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

    public Uni<Void> createOrder(final OrderEntity requestOrder) {
        final TransientHolder holder = new TransientHolder();
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
                        requestOrder.setItems(orderItems);

                        holder.setProductsEntities(updatedProducts);
                        return Uni.createFrom().nullItem();
                    });
                })
                .onItem().transformToUni(v -> {
                    List<ProductEntity> products = holder.getProductsEntities();
                    return productRepository.persistAll(products).onItem().transform(savedProducts -> {
                        holder.setProductsEntities(savedProducts);
                        return null;
                    });
                })
                .onItem().transformToUni(v -> {
                    return storeConfigRepository.findByConfigKey(ConfigKey.TAX_PERCENT)
                            .onItem()
                            .transform(taxConfig -> {
                                holder.putConfig(ConfigKey.TAX_PERCENT, taxConfig);
                                return null;
                            });
                })
                .onItem().transformToUni(v -> {
                    StoreConfigEntity taxConfig = holder.getConfig(ConfigKey.TAX_PERCENT);

                    validateOrderEntity(requestOrder, taxConfig, holder);

                    Uni<List<OrderItemEntity>> itemsUni = orderItemRepository.persistAll(requestOrder.getItems());
                    Uni<OrderEntity> orderUni = orderRepository.persist(requestOrder);

                    return Uni.combine().all().unis(orderUni, itemsUni).asTuple().onItem().transform(vv -> null);
                });
    }

    private void validateOrderEntity(OrderEntity requestEntity,
                                     StoreConfigEntity taxConfig,
                                     TransientHolder holder) {
        Map<Integer, ProductEntity> productMap = holder.getProductsAsMap();

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
        due = paid - subtotal;

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
}
