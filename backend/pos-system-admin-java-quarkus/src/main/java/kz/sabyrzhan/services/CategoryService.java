package kz.sabyrzhan.services;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.entities.CategoryEntity;
import kz.sabyrzhan.exceptions.EntityAlreadyExistsException;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CategoryService {
    private static final int ITEMS_PER_PAGE = 10;

    public Uni<CategoryEntity> addCategory(CategoryEntity entity) {
        return CategoryEntity.<CategoryEntity>find("name = ?1", entity.getName()).count()
                .onItem().transformToUni(count -> {
                    if (count == 0) {
                        return Panache.withTransaction(entity::persist);
                    } else {
                        return Uni.createFrom().failure(new EntityAlreadyExistsException("Category with this name already exists"));
                    }
                });
    }

    public Multi<CategoryEntity> getList(int page) {
        return CategoryEntity.<CategoryEntity>find("order by name")
                .page(page - 1,ITEMS_PER_PAGE)
                .stream();
    }
}
