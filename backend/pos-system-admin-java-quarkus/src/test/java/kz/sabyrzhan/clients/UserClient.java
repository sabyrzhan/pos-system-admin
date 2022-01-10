package kz.sabyrzhan.clients;

import kz.sabyrzhan.entities.UserEntity;
import kz.sabyrzhan.model.User;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/api/v1/users")
@RegisterRestClient(baseUri = "http://localhost:8081")
public interface UserClient {
    @POST
    User createUser(UserEntity user);
}
