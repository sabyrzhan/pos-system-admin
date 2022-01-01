package kz.sabyrzhan.services;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.entities.UserEntity;
import kz.sabyrzhan.exceptions.UserAlreadyExistsException;
import kz.sabyrzhan.exceptions.UserNotFoundException;
import kz.sabyrzhan.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserService {
    public Uni<User> findByUsernameAndPassword(String username, String password) {
        return UserEntity.<UserEntity>find("username = ?1 and password = ?2", username, password).singleResult()
                .onItem().transform(User::fromEntity)
                .onFailure().transform(throwable -> new UserNotFoundException(throwable));
    }

    public Uni<User> findById(int id) {
        return UserEntity.<UserEntity>findById(id)
                .onItem().transform(User::fromEntity)
                .onFailure().transform(t -> new UserNotFoundException(t));
    }

    public Uni<User> createUser(UserEntity user) {
        return UserEntity.<UserEntity>find("username = ?1", user.getUsername()).count()
                .onItem().transformToUni(count -> {
                    if (count == 0) {
                        return createUserEntity(user);
                    } else {
                        return Uni.createFrom().failure(new UserAlreadyExistsException());
                    }
                })
                .onItem().transform(User::fromEntity);
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
