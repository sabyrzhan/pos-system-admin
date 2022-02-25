package kz.sabyrzhan.resources;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.entities.OrderEntity;
import kz.sabyrzhan.model.InvoiceResult;
import kz.sabyrzhan.model.InvoiceType;
import kz.sabyrzhan.model.OrderStatus;
import kz.sabyrzhan.repositories.OrderItemRepository;
import kz.sabyrzhan.repositories.OrderRepository;
import kz.sabyrzhan.services.InvoiceService;
import kz.sabyrzhan.services.OrderService;
import kz.sabyrzhan.services.dto.TransientHolder;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

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
    @Counted("orders.create.counted")
    @Timed("orders.create.timed")
    public Uni<OrderEntity> createOrder(OrderEntity order) {
        return orderService.createOrder(order);
    }

    @GET
    @Path("/{id}")
    @Counted("orders.byid.counted")
    @Timed("orders.byid.timed")
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
    @Counted("orders.list.counted")
    @Timed("orders.list.timed")
    public Multi<OrderEntity> getList(@QueryParam("page") int page) {
        if (page == 0) {
            page = 1;
        }

        return orderRepository.list(page, 30);
    }

    @DELETE
    @Path("/{orderId}")
    @Counted("orders.cancel.counted")
    @Timed("orders.cancel.timed")
    public Uni<OrderEntity> cancelOrder(@PathParam("orderId") String orderId) {
        return orderService.cancelOrder(orderId);
    }

    @PUT
    @Path("/{orderId}/start")
    @Counted("orders.start.counted")
    @Timed("orders.start.timed")
    public Uni<Response> startOrder(@PathParam("orderId") String orderId) {
        return orderRepository.updateStatus(orderId, OrderStatus.PROCESSING).onItem().transform(v -> Response.ok().build());
    }

    @PUT
    @Path("/{orderId}/finish")
    @Counted("orders.finish.counted")
    @Timed("orders.finish.timed")
    public Uni<Response> finishOrder(@PathParam("orderId") String orderId) {
        return orderRepository.updateStatus(orderId, OrderStatus.DONE).onItem().transform(v -> Response.ok().build());
    }

    @POST
    @Path("/{orderId}/invoice")
    @Counted("orders.invoice_generate.counted")
    @Timed("orders.invoice_generate.timed")
    public Uni<InvoiceResult> generateOrderInvoice(@PathParam("orderId") final String orderId,
                                                   @QueryParam("type") final InvoiceType type) {
        return getById(orderId)
                .onItem()
                .transformToUni(orderEntity -> invoiceService.generateInvoice(orderEntity, type != null ? type : InvoiceType.STANDARD));
    }
}
