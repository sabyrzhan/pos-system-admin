package kz.sabyrzhan.resources;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.entities.OrderEntity;
import kz.sabyrzhan.repositories.OrderRepository;
import kz.sabyrzhan.services.OrderService;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;

@Path("/api/v1/orders")
@ApplicationScoped
@Slf4j
public class OrdersResource {
    @Inject
    OrderService orderService;

    @Inject
    OrderRepository orderRepository;

    @POST
    public Uni<OrderEntity> createOrder(OrderEntity order) {
        return orderService.createOrder(order);
    }

    @GET
    @Path("/{id}")
    public Uni<OrderEntity> getById(@PathParam("id") String id) {
        return orderRepository.findById(id);
    }

    @GET
    public Multi<OrderEntity> getList(@QueryParam("page") int page) {
        if (page == 0) {
            page = 1;
        }

        return orderRepository.list(page, 30);
    }

    @DELETE
    @Path("/{orderId}")
    public Uni<OrderEntity> cancelOrder(@PathParam("orderId") String orderId) {
        return orderRepository.cancelOrder(orderId);
    }
}
