package kz.sabyrzhan.resources;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.entities.OrderEntity;
import kz.sabyrzhan.model.InvoiceResult;
import kz.sabyrzhan.repositories.OrderItemRepository;
import kz.sabyrzhan.repositories.OrderRepository;
import kz.sabyrzhan.services.InvoiceService;
import kz.sabyrzhan.services.OrderService;
import kz.sabyrzhan.services.dto.TransientHolder;
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

    @Inject
    OrderItemRepository orderItemRepository;

    @Inject
    InvoiceService invoiceService;

    @POST
    public Uni<OrderEntity> createOrder(OrderEntity order) {
        return orderService.createOrder(order);
    }

    @GET
    @Path("/{id}")
    public Uni<OrderEntity> getById(@PathParam("id") String id) {
        var holder = new TransientHolder();
        return orderRepository.findById(id)
                .onItem().transformToUni(orderEntity -> {
                    holder.setOrderEntity(orderEntity);
                    return orderItemRepository.findByOrderId(orderEntity.getId());
                })
                .onItem().transform(items -> {
                    holder.getOrderEntity().setItems(items);
                    return holder.getOrderEntity();
                });
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
        return orderService.cancelOrder(orderId);
    }

    @POST
    @Path("/{orderId}/invoice")
    public Uni<InvoiceResult> generateOrderInvoice(@PathParam("orderId") String orderId) {
        return getById(orderId).onItem().transformToUni(orderEntity -> invoiceService.generatePdf(orderEntity));
    }
}
