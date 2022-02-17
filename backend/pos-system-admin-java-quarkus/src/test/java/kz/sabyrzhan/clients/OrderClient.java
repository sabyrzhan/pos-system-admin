package kz.sabyrzhan.clients;

import kz.sabyrzhan.entities.OrderEntity;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;

@Path("/api/v1/orders")
@RegisterRestClient(baseUri = "http://localhost:8081")
public interface OrderClient {
    @POST
    OrderEntity create(OrderEntity entity);

    @GET
    @Path("/{id}")
    OrderEntity getById(@PathParam("id") String id);

    @DELETE
    @Path("/{id}")
    OrderEntity cancel(@PathParam("id") String id);

    @PUT
    @Path("/{orderId}/start")
    void startOrder(@PathParam("orderId") String id);

    @PUT
    @Path("/{orderId}/finish")
    void finishOrder(@PathParam("orderId") String id);
}
