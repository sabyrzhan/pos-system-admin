package kz.sabyrzhan.resources;

import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.entities.ProductEntity;
import kz.sabyrzhan.model.DashboardInfo;
import kz.sabyrzhan.model.OrderStatus;
import kz.sabyrzhan.model.ProductWithCount;
import kz.sabyrzhan.repositories.OrderRepository;
import kz.sabyrzhan.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Path("/api/v1/dashboard")
@ApplicationScoped
@Slf4j
public class DashboardResource {
    @Inject
    OrderRepository orderRepository;
    @Inject
    ProductRepository productRepository;

    @GET
    @Path("/info")
    public Uni<DashboardInfo> info() {
        final DashboardInfo info = new DashboardInfo();
        return orderRepository.countOrders(OrderStatus.NEW)
                .onItem().transformToUni(c -> {
                    info.setTotalNewOrders(c);
                    return orderRepository.countOrders(OrderStatus.DONE);
                })
                .onItem().transformToUni(c -> {
                    info.setTotalProcessedOrders(c);
                    return orderRepository.countOrders(OrderStatus.CANCELLED);
                })
                .onItem().transformToUni(c -> {
                    info.setTotalCancelledOrders(c);
                    return orderRepository.countOrders(OrderStatus.RETURNED);
                })
                .onItem().transformToUni(c -> {
                    info.setTotalReturnedOrders(c);
                    return orderRepository.countOrders(OrderStatus.PROCESSING);
                })
                .onItem().transformToUni(c -> {
                    info.setTotalInProgressOrders(c);
                    return productRepository.countAvailableProducts();
                })
                .onItem().transformToUni(c -> {
                    info.setTotalProducts(c);
                    return productRepository.countTopOrderedProducts();
                })
                .onItem().transformToUni(productWithCounts -> {
                    Set<Integer> productIds = productWithCounts.stream().map(ProductWithCount::getId).collect(Collectors.toSet());
                    return productRepository.list(productIds)
                            .onItem()
                            .transform(products -> {
                                var productEntityMap = products.stream().collect(Collectors.toMap(ProductEntity::getId, Function.identity()));
                                var topProducts = new HashMap<String, Long>();
                                for(ProductWithCount c : productWithCounts) {
                                    if (productEntityMap.get(c.getId()) == null) {
                                        continue;
                                    }
                                    topProducts.put(productEntityMap.get(c.getId()).getName(), c.getOrderCount());
                                }
                                info.setTopProducts(topProducts);
                                return info;
                            });
                });
    }
}
