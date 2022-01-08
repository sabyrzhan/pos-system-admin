package kz.sabyrzhan.resources;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.model.UserRole;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/api/v1/dict")
@ApplicationScoped
@Slf4j
public class DictionaryResource {
    @GET
    @Path("/roles")
    public Multi<UserRole> roles() {
        return Multi.createFrom().items(UserRole.values());
    }
}
