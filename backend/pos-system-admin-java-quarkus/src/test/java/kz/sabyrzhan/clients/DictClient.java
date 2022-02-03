package kz.sabyrzhan.clients;

import kz.sabyrzhan.entities.CategoryEntity;
import kz.sabyrzhan.entities.StoreConfigEntity;
import kz.sabyrzhan.model.ConfigKey;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
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

    @POST
    @Path("/configs")
    StoreConfigEntity create(StoreConfigEntity entity);

    @GET
    @Path("/configs/{configKey}")
    StoreConfigEntity getByConfigKey(@PathParam("configKey") ConfigKey configKey);
}
