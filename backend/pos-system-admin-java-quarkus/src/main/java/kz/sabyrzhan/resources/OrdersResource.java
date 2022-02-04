package kz.sabyrzhan.resources;

import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.entities.OrderEntity;
import kz.sabyrzhan.services.OrderService;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/api/v1/orders")
@ApplicationScoped
@Slf4j
public class OrdersResource {
    @Inject
    OrderService orderService;

    @POST
    public Uni<OrderEntity> createOrder(OrderEntity order) {
        return orderService.createOrder(order);
    }
}
