package kz.sabyrzhan.services;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.entities.CategoryEntity;
import kz.sabyrzhan.exceptions.EntityAlreadyExistsException;
import kz.sabyrzhan.repositories.CategoryRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class CategoryService {
    private static final int ITEMS_PER_PAGE = 10;
    public static final String CATEGORY_NAME_ALREADY_EXISTS = "Category with this name already exists";

    @Inject
    CategoryRepository categoryRepository;

    public Uni<CategoryEntity> addCategory(CategoryEntity entity) {
        return CategoryEntity.<CategoryEntity>find("name = ?1", entity.getName()).count()
                .onItem().transformToUni(count -> {
                    if (count == 0) {
                        return Panache.withTransaction(entity::persist);
                    } else {
                        return Uni.createFrom().failure(new EntityAlreadyExistsException(CATEGORY_NAME_ALREADY_EXISTS));
                    }
                });
    }

    public Uni<CategoryEntity> updateCategory(CategoryEntity entity) {
        return categoryRepository.findById(entity.getId())
                .onItem()
                .transformToUni(foundCategory -> categoryRepository.findByName(entity.getName())
                            .onItemOrFailure()
                            .transformToUni((v,t) -> {
                                if (v != null && v.getId() != entity.getId()) {
                                    return Uni.createFrom().failure(new EntityAlreadyExistsException(CATEGORY_NAME_ALREADY_EXISTS));
                                }
                                foundCategory.setName(entity.getName());
                                return categoryRepository.persist(foundCategory);
                            })
                );
    }

    public Multi<CategoryEntity> getList(int page) {
        return CategoryEntity.<CategoryEntity>find("order by name")
                .page(page - 1,ITEMS_PER_PAGE)
                .stream();
    }

    public Uni<List<CategoryEntity>> getAll() {
        return CategoryEntity.<CategoryEntity>findAll().list();
    }

    public Uni<CategoryEntity> getCategory(int id) {
        return categoryRepository.findById(id);
    }
}
