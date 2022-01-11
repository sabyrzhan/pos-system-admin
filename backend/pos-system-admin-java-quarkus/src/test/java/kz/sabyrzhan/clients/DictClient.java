package kz.sabyrzhan.clients;

import kz.sabyrzhan.entities.CategoryEntity;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.List;

@Path("/api/v1/dict")
@RegisterRestClient(baseUri = "http://localhost:8081")
public interface DictClient {
    @GET
    @Path("/categories")
    List<CategoryEntity> getList(@QueryParam("page") int page);

    @POST
    @Path("/categories")
    CategoryEntity create(CategoryEntity entity);
}
