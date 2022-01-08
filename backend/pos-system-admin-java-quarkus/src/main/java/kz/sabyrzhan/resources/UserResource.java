package kz.sabyrzhan.resources;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.entities.UserEntity;
import kz.sabyrzhan.model.User;
import kz.sabyrzhan.resources.request.UsernameAndPasswordRequest;
import kz.sabyrzhan.services.UserService;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/api/v1/users")
@ApplicationScoped
@Slf4j
public class UserResource {
    @Inject
    UserService userService;

    @POST
    public Uni<User> createUser(UserEntity user) {
        return userService.createUser(user);
    }

    @GET
    public Multi<User> getUsers(@QueryParam("page") int page) {
        if (page == 0) {
            page = 1;
        }

        return userService.findUsers(page);
    }

    @GET
    @Path("/{id}")
    public Uni<User> getById(@PathParam("id") int id) {
        return userService.findById(id);
    }

    @POST
    @Path("/validate")
    public Uni<Response> validateUserByUsernameAndPassword(UsernameAndPasswordRequest request) {
        return userService.findByUsernameAndPassword(request.getUsername(), request.getPassword())
                .onItem().transform(user -> Response.ok(user).build());
    }

    @POST
    @Path("/{id}/change_password")
    public Uni<Response> changePassword(@PathParam("id") int id, UserEntity userIdAndPassword) {
        userIdAndPassword.setId(id);
        return userService.changePassword(userIdAndPassword).onItem().transform(user -> Response.ok(user).build())
                .onFailure().recoverWithItem(throwable -> Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse(throwable.getMessage()))
                        .build());
    }
}
