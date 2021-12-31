package kz.sabyrzhan.services;

import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.entities.UserEntity;
import kz.sabyrzhan.exceptions.UserNotFoundException;
import kz.sabyrzhan.model.User;

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
}
