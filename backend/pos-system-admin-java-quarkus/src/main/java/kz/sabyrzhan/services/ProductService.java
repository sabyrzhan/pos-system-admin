package kz.sabyrzhan.services;

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.entities.CategoryEntity;
import kz.sabyrzhan.entities.ProductEntity;
import kz.sabyrzhan.exceptions.EntityAlreadyExistsException;
import kz.sabyrzhan.model.Product;
import kz.sabyrzhan.repositories.CategoryRepository;
import kz.sabyrzhan.repositories.ProductRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ProductService {
    public static final int DEFAULT_SIZE_PER_PAGE = 10;
    public static final String PRODUCT_NAME_ALREADY_EXISTS = "Product with this name already exists";

    @Inject
    ProductRepository productRepository;

    @Inject
    CategoryRepository categoryRepository;

    public Uni<ProductEntity> getProduct(int id) {
        return productRepository.findById(id);
    }

    @ReactiveTransactional
    public Uni<ProductEntity> addProduct(ProductEntity entity) {
        return productRepository.findByName(entity.getName())
                .onItemOrFailure()
                .transformToUni((v, t) -> {
                    if (v != null) {
                        return Uni.createFrom().failure(new EntityAlreadyExistsException(PRODUCT_NAME_ALREADY_EXISTS));
                    }

                    return productRepository.persist(entity);
                });
    }

    @ReactiveTransactional
    public Uni<Product> updateProduct(ProductEntity entity) {
        Uni<ProductEntity> productUni = productRepository.findById(entity.getId());
        Uni<CategoryEntity> categoryUni = categoryRepository.findById(entity.getCategoryId());
        return Uni.combine().all().unis(productUni, categoryUni).asTuple()
                .onItem().transformToUni(tuple -> {
                    var foundCategory = tuple.getItem2();
                    var foundProduct = tuple.getItem1();

                    return productRepository.findByName(entity.getName())
                            .onItemOrFailure().transformToUni((v, t) -> {
                                if (v != null && v.getId() != entity.getId()) {
                                    return Uni.createFrom().failure(new EntityAlreadyExistsException(PRODUCT_NAME_ALREADY_EXISTS));
                                }

                                foundProduct.setName(entity.getName());
                                foundProduct.setCategoryId(foundCategory.getId());
                                foundProduct.setPurchasePrice(entity.getPurchasePrice());
                                foundProduct.setSalePrice(entity.getSalePrice());
                                foundProduct.setStock(entity.getStock());
                                foundProduct.setDescription(entity.getDescription());
                                foundProduct.setImages(entity.getImages());
                                return productRepository.persist(foundProduct).onItem().transform(updated -> Product.fromEntity(updated, foundCategory));
                            });
                });
    }

    public Multi<ProductEntity> getProducts(int page, int sizePerPage) {
        if (sizePerPage == 0) {
            sizePerPage = DEFAULT_SIZE_PER_PAGE;
        }

        return productRepository.findProducts(page, sizePerPage);
    }
}
