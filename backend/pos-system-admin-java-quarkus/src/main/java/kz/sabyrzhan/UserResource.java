package kz.sabyrzhan;

import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.model.User;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/api/v1/users")
public class UserResource {
    @POST
    public Uni<Response> createUser() {
        return Uni.createFrom().item(Response.accepted().build());
    }

    @GET
    @Path("/{id}")
    public Uni<User> getById(@PathParam("id") long id) {
        return Uni.createFrom().item(new User());
    }
}
