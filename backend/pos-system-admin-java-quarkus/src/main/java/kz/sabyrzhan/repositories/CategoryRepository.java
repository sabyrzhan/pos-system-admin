package kz.sabyrzhan.repositories;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.entities.CategoryEntity;
import kz.sabyrzhan.exceptions.EntityNotFoundException;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CategoryRepository implements PanacheRepositoryBase<CategoryEntity, Integer> {
    public static final String CATEGORY_NOT_FOUND_MESSAGE = "Category not found";

    public Uni<CategoryEntity> findById(int id) {
        return CategoryEntity.<CategoryEntity>findById(id).onItem().transformToUni(categoryEntity -> {
            if (categoryEntity == null) {
                return Uni.createFrom().failure(new EntityNotFoundException(CATEGORY_NOT_FOUND_MESSAGE));
            }

            return Uni.createFrom().item(categoryEntity);
        });
    }

    public Uni<CategoryEntity> findByName(String name) {
        return CategoryEntity.<CategoryEntity>find("name = ?1", name).list()
                .onItem().transformToUni(items -> {
                    if (items.size() == 0) {
                        return Uni.createFrom().failure(new EntityNotFoundException(CATEGORY_NOT_FOUND_MESSAGE));
                    }

                    return Uni.createFrom().item(items.get(0));
                });
    }

    public Uni<CategoryEntity> persist(CategoryEntity entity) {
        return Panache.withTransaction(entity::persist);
    }
}
