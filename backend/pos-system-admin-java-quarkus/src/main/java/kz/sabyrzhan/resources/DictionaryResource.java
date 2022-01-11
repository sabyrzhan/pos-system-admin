package kz.sabyrzhan.resources;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.entities.CategoryEntity;
import kz.sabyrzhan.model.UserRole;
import kz.sabyrzhan.services.CategoryService;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("/api/v1/dict")
@ApplicationScoped
@Slf4j
public class DictionaryResource {
    @Inject
    CategoryService categoryService;

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


}
