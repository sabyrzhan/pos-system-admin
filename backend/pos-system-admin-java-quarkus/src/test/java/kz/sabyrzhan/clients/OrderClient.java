package kz.sabyrzhan.clients;

import kz.sabyrzhan.entities.OrderEntity;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/api/v1/orders")
@RegisterRestClient(baseUri = "http://localhost:8081")
public interface OrderClient {
    @POST
    OrderEntity create(OrderEntity entity);

    @GET
    @Path("/{id}")
    OrderEntity getById(@PathParam("id") String id);
}
