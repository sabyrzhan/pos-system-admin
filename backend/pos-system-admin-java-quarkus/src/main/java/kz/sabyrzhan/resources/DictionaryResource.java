package kz.sabyrzhan.resources;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.entities.CategoryEntity;
import kz.sabyrzhan.entities.StoreConfigEntity;
import kz.sabyrzhan.model.ConfigKey;
import kz.sabyrzhan.model.PaymentType;
import kz.sabyrzhan.model.UserRole;
import kz.sabyrzhan.repositories.StoreConfigRepository;
import kz.sabyrzhan.services.CategoryService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;

@Path("/api/v1/dict")
@ApplicationScoped
public class DictionaryResource {
    @Inject
    CategoryService categoryService;

    @Inject
    StoreConfigRepository storeConfigRepository;

    @GET
    @Path("/roles")
    @Counted("dictionary.roles.counted")
    @Timed("dictionary.roles.timed")
    public Multi<UserRole> roles() {
        return Multi.createFrom().items(UserRole.values());
    }

    @POST
    @Path("/categories")
    @Counted("dictionary.category_create.counted")
    @Timed("dictionary.category_create.timed")
    public Uni<CategoryEntity> addCategory(CategoryEntity category) {
        return categoryService.addCategory(category);
    }

    @GET
    @Path("/categories")
    @Counted("dictionary.categories.counted")
    @Timed("dictionary.categories.timed")
    public Multi<CategoryEntity> getCategories(@QueryParam("page") int page) {
        if (page == 0) {
            page = 1;
        }

        return categoryService.getList(page);
    }

    @GET
    @Path("/categories/{id}")
    @Counted("dictionary.category_byid.counted")
    @Timed("dictionary.category_byid.timed")
    public Uni<CategoryEntity> getCategoryById(@PathParam("id") int id) {
        return categoryService.getCategory(id);
    }

    @PUT
    @Path("/categories/{id}")
    @Counted("dictionary.category_update.counted")
    @Timed("dictionary.category_update.timed")
    public Uni<CategoryEntity> updateCategory(@PathParam("id") int id, CategoryEntity entity) {
        return categoryService.updateCategory(entity);
    }

    @GET
    @Path("/configs/{configKey}")
    @Counted("dictionary.config_by_key.counted")
    @Timed("dictionary.config_by_key.timed")
    public Uni<StoreConfigEntity> getConfigs(@PathParam("configKey") ConfigKey configKey) {
        return storeConfigRepository.findByConfigKey(configKey);
    }

    @POST
    @Path("/configs")
    @Counted("dictionary.config_create.counted")
    @Timed("dictionary.config_create.timed")
    @ReactiveTransactional
    public Uni<StoreConfigEntity> addConfig(StoreConfigEntity entity) {
        return storeConfigRepository.persist(entity);
    }

    @GET
    @Path("/paymentTypes")
    @Counted("dictionary.payment_types.counted")
    @Timed("dictionary.payment_types.timed")
    public Uni<PaymentType[]> getPaymentTypes() {
        return Uni.createFrom().item(PaymentType.values());
    }
}
