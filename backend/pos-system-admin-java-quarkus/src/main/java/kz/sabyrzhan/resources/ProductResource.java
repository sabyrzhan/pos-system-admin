package kz.sabyrzhan.resources;

import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.entities.ProductEntity;
import kz.sabyrzhan.services.ProductService;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/api/v1/products")
@ApplicationScoped
@Slf4j
public class ProductResource {
    @Inject
    ProductService productService;

    @GET
    @Path("/{id}")
    public Uni<ProductEntity> getById(@PathParam("id") int id) {
        return productService.getProduct(id);
    }

    @POST
    public Uni<ProductEntity> addProduct(ProductEntity entity) {
        return productService.addProduct(entity);
    }
}
