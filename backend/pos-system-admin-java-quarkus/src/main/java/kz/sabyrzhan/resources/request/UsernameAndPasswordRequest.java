package kz.sabyrzhan.resources.request;

import lombok.Data;

@Data
public class UsernameAndPasswordRequest {
    private String username;
    private String password;
}
