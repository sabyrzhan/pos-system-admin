package kz.sabyrzhan.clients;

import kz.sabyrzhan.entities.ProductEntity;
import kz.sabyrzhan.model.Product;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import java.util.List;

@Path("/api/v1/products")
@RegisterRestClient(baseUri = "http://localhost:8081")
public interface ProductClient {
    @GET
    @Path("/{id}")
    Product getById(@PathParam("id") int id);

    @POST
    ProductEntity addProduct(ProductEntity entity);
}
