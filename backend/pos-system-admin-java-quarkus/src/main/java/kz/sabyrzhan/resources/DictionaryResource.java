package kz.sabyrzhan.resources;

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.entities.CategoryEntity;
import kz.sabyrzhan.entities.StoreConfigEntity;
import kz.sabyrzhan.model.ConfigKey;
import kz.sabyrzhan.model.UserRole;
import kz.sabyrzhan.repositories.StoreConfigRepository;
import kz.sabyrzhan.services.CategoryService;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;

@Path("/api/v1/dict")
@ApplicationScoped
@Slf4j
public class DictionaryResource {
    @Inject
    CategoryService categoryService;

    @Inject
    StoreConfigRepository storeConfigRepository;

    @GET
    @Path("/roles")
    public Multi<UserRole> roles() {
        return Multi.createFrom().items(UserRole.values());
    }

    @POST
    @Path("/categories")
    public Uni<CategoryEntity> addCategory(CategoryEntity category) {
        return categoryService.addCategory(category);
    }

    @GET
    @Path("/categories")
    public Multi<CategoryEntity> getCategories(@QueryParam("page") int page) {
        if (page == 0) {
            page = 1;
        }

        return categoryService.getList(page);
    }

    @GET
    @Path("/categories/{id}")
    public Uni<CategoryEntity> getCategoryById(@PathParam("id") int id) {
        return categoryService.getCategory(id);
    }

    @PUT
    @Path("/categories/{id}")
    public Uni<CategoryEntity> updateCategory(@PathParam("id") int id, CategoryEntity entity) {
        return categoryService.updateCategory(entity);
    }

    @GET
    @Path("/configs/{configKey}")
    public Uni<StoreConfigEntity> getConfigs(@PathParam("configKey") ConfigKey configKey) {
        return storeConfigRepository.findByConfigKey(configKey);
    }

    @POST
    @Path("/configs")
    @ReactiveTransactional
    public Uni<StoreConfigEntity> addConfig(StoreConfigEntity entity) {
        return storeConfigRepository.persist(entity);
    }
}
