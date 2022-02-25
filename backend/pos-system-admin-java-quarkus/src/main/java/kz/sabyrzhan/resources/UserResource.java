package kz.sabyrzhan.resources;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/v1/users")
@ApplicationScoped
@Slf4j
public class UserResource {
    @Inject
    UserService userService;

    @POST
    @Counted("users.create.counted")
    @Timed("users.create.timed")
    public Uni<User> createUser(UserEntity user) {
        return userService.createUser(user);
    }

    @GET
    @Counted("users.list.counted")
    @Timed("users.list.timed")
    public Multi<User> getUsers(@QueryParam("page") int page) {
        if (page == 0) {
            page = 1;
        }

        return userService.findUsers(page);
    }

    @GET
    @Path("/{id}")
    @Counted("users.byid.counted")
    @Timed("users.byid.timed")
    public Uni<User> getById(@PathParam("id") int id) {
        return userService.findById(id);
    }

    @POST
    @Path("/validate")
    @Counted("users.validate.counted")
    @Timed("users.validate.timed")
    public Uni<Response> validateUserByUsernameAndPassword(UsernameAndPasswordRequest request) {
        return userService.findByUsernameAndPassword(request.getUsername(), request.getPassword())
                .onItem().transform(user -> Response.ok(user).build());
    }

    @POST
    @Path("/{id}/change_password")
    @Counted("users.change_password.counted")
    @Timed("users.change_password.timed")
    public Uni<Response> changePassword(@PathParam("id") int id, UserEntity userIdAndPassword) {
        userIdAndPassword.setId(id);
        return userService.changePassword(userIdAndPassword).onItem().transform(user -> Response.ok(user).build())
                .onFailure().recoverWithItem(throwable -> Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse(throwable.getMessage()))
                        .build());
    }

    @DELETE
    @Path("/{id}")
    @Counted("users.delete.counted")
    @Timed("users.delete.timed")
    public Uni<Response> deleteUser(@PathParam("id") int id) {
        return userService.deleteUser(id)
                .onItem().transform(v -> Response.accepted().type(MediaType.APPLICATION_JSON_TYPE).build());
    }
}
