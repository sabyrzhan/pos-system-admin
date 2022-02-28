package kz.sabyrzhan.services;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.entities.UserEntity;
import kz.sabyrzhan.exceptions.EntityAlreadyExistsException;
import kz.sabyrzhan.exceptions.EntityNotFoundException;
import kz.sabyrzhan.model.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
                .onItem().transformToUni(u -> {
                    if (u == null) {
                        return Uni.createFrom().failure(new EntityNotFoundException("User not found"));
                    }

                    return Uni.createFrom().item(User.fromEntity(u));
                });
    }

    public Uni<User> findById(int id) {
        return UserEntity.<UserEntity>findById(id)
                .onItem().transformToUni(u -> {
                    if (u == null) {
                        return Uni.createFrom().failure(new EntityNotFoundException("User not found"));
                    }

                    return Uni.createFrom().item(User.fromEntity(u));
                });
    }

    public Uni<User> createUser(UserEntity user) {
        return UserEntity.<UserEntity>find("username = ?1 or email = ?2", user.getUsername(), user.getEmail()).count()
                .onItem().transformToUni(count -> {
                    if (count == 0) {
                        user.setSalt(generateSalt());
                        return Panache.<UserEntity>withTransaction(user::persist);
                    } else {
                        return Uni.createFrom().failure(new EntityAlreadyExistsException("User already exists"));
                    }
                })
                .onItem().transform(User::fromEntity);
    }

    public Uni<User> changePassword(UserEntity userIdAndPassword) {
        return UserEntity.<UserEntity>findById(userIdAndPassword.getId())
                .onItem().transformToUni(userEntity -> {
                    userEntity.setSalt(generateSalt());
                    userEntity.setPassword(userIdAndPassword.getPassword());
                    return Panache.<UserEntity>withTransaction(userEntity::persist);
                })
                .onItem().transform(User::fromEntity)
                .onFailure().transform(t -> new EntityNotFoundException("User not found"));
    }

    public Uni<Boolean> deleteUser(int id) {
        return Panache.withTransaction(() -> UserEntity.deleteById(id));
    }

    private String generateSalt() {
        List<Character> chars = new ArrayList<>();
        for(char i = 'a'; i <= 'z'; i++) {
            chars.add(i);
        }
        for(char i = 'A'; i <= 'Z'; i++) {
            chars.add(i);
        }
        for(char i = '0'; i <= '9'; i++) {
            chars.add(i);
        }
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        for(int i = 0; i < 13; i++) {
            sb.append(chars.get(r.nextInt(chars.size())));
        }
        return sb.toString();
    }
}
