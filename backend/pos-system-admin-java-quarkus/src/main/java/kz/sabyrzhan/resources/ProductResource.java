package kz.sabyrzhan.resources;

import io.micrometer.core.annotation.Timed;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import kz.sabyrzhan.entities.CategoryEntity;
import kz.sabyrzhan.entities.ProductEntity;
import kz.sabyrzhan.model.Product;
import kz.sabyrzhan.services.CategoryService;
import kz.sabyrzhan.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Path("/api/v1/products")
@ApplicationScoped
@Slf4j
public class ProductResource {
    @Inject
    ProductService productService;

    @Inject
    CategoryService categoryService;

    @Inject
    RateLimitManager rateLimitManager;

    @GET
    @ReactiveTransactional
    @Timed(value = "products.list")
    public Uni<List<Product>> getProducts(@QueryParam("page") final int page,
                                          @QueryParam("sizePerPage") final int sizePerPage) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        return rateLimitManager.acquire()
                .onItem().transformToUni(v -> {
                    Uni<List<CategoryEntity>> allCategories = categoryService.getAll();
                    Uni<List<ProductEntity>> allProducts = productService.getProducts(
                                                                    page == 0 ? 1 : page,
                                                                    sizePerPage == 0 ? 30 : sizePerPage).collect().asList();


                    return Uni.combine().all().unis(allCategories, allProducts).asTuple();
                })
                .onItem().transform(objects -> {
                    Map<Integer, CategoryEntity> categories = objects.getItem1().stream().collect(Collectors.toMap(CategoryEntity::getId, Function.identity()));
                    return Tuple2.of(categories, objects.getItem2());
                })
                .onItem().transform(objects -> {
                    var categoriesMap = objects.getItem1();
                    var result = objects.getItem2().stream().map(entity -> {
                        var product = Product.fromEntity(entity, categoriesMap.get(entity.getCategoryId()));
                        return product;
                    }).collect(Collectors.toList());

                    stopWatch.stop();
                    log.info("Rate limit acquire time: {}", stopWatch.toString());
                    return result;
                });
    }

    @GET
    @Path("/{id}")
    public Uni<Product> getById(@PathParam("id") int id) {
        return productService.getProduct(id)
                .onItem()
                .transformToUni(product -> categoryService.getCategory(product.getCategoryId())
                                                .onItem()
                                                .transform(category -> Product.fromEntity(product, category))
                );
    }

    @POST
    public Uni<ProductEntity> addProduct(ProductEntity entity) {
        return productService.addProduct(entity);
    }

    @PUT
    @Path("/{id}")
    public Uni<Product> updateProduct(@PathParam("id") int id, ProductEntity entity) {
        entity.setId(id);
        return productService.updateProduct(entity);
    }
}
