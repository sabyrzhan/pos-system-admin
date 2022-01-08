package kz.sabyrzhan.model;

import kz.sabyrzhan.entities.UserEntity;
import lombok.Data;

@Data
public class User {
    private int id;
    private String username;
    private String email;
    private String role;

    public static User fromEntity(UserEntity userEntity) {
        User user = new User();
        user.setId(userEntity.getId());
        user.setUsername(userEntity.getUsername());
        user.setEmail(userEntity.getEmail());
        user.setRole(userEntity.getRole());

        return user;
    }
}
