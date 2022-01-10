package kz.sabyrzhan.services;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.entities.UserEntity;
import kz.sabyrzhan.exceptions.UserAlreadyExistsException;
import kz.sabyrzhan.exceptions.UserNotFoundException;
import kz.sabyrzhan.model.User;
import org.apache.commons.lang3.RandomStringUtils;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserService {
    private static final int ITEMS_PER_PAGE = 10;

    public Multi<User> findUsers(int page) {
        return UserEntity.<UserEntity>find("order by username")
                .page(page - 1,ITEMS_PER_PAGE)
                .stream().map(User::fromEntity);
    }

    public Uni<User> findByUsernameAndPassword(String username, String password) {
        return UserEntity.<UserEntity>find("(username = ?1 or email = ?1)and password = ?2", username, password).singleResult()
                .onItem().transform(User::fromEntity)
                .onFailure().transform(throwable -> new UserNotFoundException(throwable));
    }

    public Uni<User> findById(int id) {
        return UserEntity.<UserEntity>findById(id)
                .onItem().transform(User::fromEntity)
                .onFailure().transform(t -> new UserNotFoundException(t));
    }

    public Uni<User> createUser(UserEntity user) {
        return UserEntity.<UserEntity>find("username = ?1 or email = ?2", user.getUsername(), user.getEmail()).count()
                .onItem().transformToUni(count -> {
                    if (count == 0) {
                        return createUserEntity(user);
                    } else {
                        return Uni.createFrom().failure(new UserAlreadyExistsException());
                    }
                })
                .onItem().transform(User::fromEntity);
    }

    public Uni<User> changePassword(UserEntity userIdAndPassword) {
        return UserEntity.<UserEntity>findById(userIdAndPassword.getId())
                .onItem().transformToUni(userEntity -> {
                    userEntity.setPassword(userIdAndPassword.getPassword());
                    return Panache.<UserEntity>withTransaction(userEntity::persist);
                })
                .onItem().transform(User::fromEntity)
                .onFailure().transform(UserNotFoundException::new);
    }

    public Uni<Boolean> deleteUser(int id) {
        return Panache.withTransaction(() -> UserEntity.deleteById(id));
    }

    private Uni<UserEntity> createUserEntity(UserEntity userEntity) {
        UserEntity save = new UserEntity();
        save.setUsername(userEntity.getUsername());
        save.setEmail(userEntity.getEmail());
        save.setPassword(userEntity.getPassword());
        save.setSalt(generateSalt());
        save.setRole(userEntity.getRole());

        return Panache.withTransaction(save::persist);
    }

    private String generateSalt() {
        return RandomStringUtils.randomAlphabetic(13);
    }
}
