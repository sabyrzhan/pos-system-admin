package kz.sabyrzhan.resources;

import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.model.User;
import kz.sabyrzhan.resources.request.UsernameAndPasswordRequest;
import kz.sabyrzhan.services.UserService;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/api/v1/users")
@ApplicationScoped
@Slf4j
public class UserResource {
    @Inject
    private UserService userService;

    @POST
    public Uni<Response> createUser() {
        return Uni.createFrom().item(Response.accepted().build());
    }

    @GET
    @Path("/{id}")
    public Uni<User> getById(@PathParam("id") long id) {
        return Uni.createFrom().item(new User());
    }

    @POST
    @Path("/validate")
    public Uni<Response> validateUserByUsernameAndPassword(UsernameAndPasswordRequest request) {
        return userService.findByUsernameAndPassword(request.getUsername(), request.getPassword())
                .onItem().transform(user -> Response.ok(user).build())
                .onFailure().recoverWithItem(throwable -> Response.status(Response.Status.BAD_REQUEST)
                                                            .entity(new ErrorResponse(throwable.getMessage()))
                                                            .build());

    }
}
