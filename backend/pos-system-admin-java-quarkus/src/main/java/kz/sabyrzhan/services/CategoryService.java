package kz.sabyrzhan.services;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.entities.CategoryEntity;
import kz.sabyrzhan.exceptions.EntityAlreadyExistsException;
import kz.sabyrzhan.exceptions.EntityNotFoundException;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CategoryService {
    private static final int ITEMS_PER_PAGE = 10;
    public static final String CATEGORY_NOT_FOUND_MESSAGE = "Category not found";

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

    public Uni<CategoryEntity> updateCategory(CategoryEntity entity) {
        return CategoryEntity.<CategoryEntity>findById(entity.getId())
                .onItem().transformToUni(foundCategory -> {
                    if (foundCategory == null) {
                        return Uni.createFrom().failure(new EntityNotFoundException(CATEGORY_NOT_FOUND_MESSAGE));
                    }

                    foundCategory.setName(entity.getName());
                    return Panache.withTransaction(foundCategory::persist);
                });
    }

    public Multi<CategoryEntity> getList(int page) {
        return CategoryEntity.<CategoryEntity>find("order by name")
                .page(page - 1,ITEMS_PER_PAGE)
                .stream();
    }

    public Uni<CategoryEntity> getCategory(int id) {
        return CategoryEntity.<CategoryEntity>findById(id).
                onItem().transformToUni(c -> {
                    if (c == null) {
                        return Uni.createFrom().failure(new EntityNotFoundException(CATEGORY_NOT_FOUND_MESSAGE));
                    }
                    return Uni.createFrom().item(c);
                });
    }
}
