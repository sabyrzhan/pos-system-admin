package kz.sabyrzhan.resources;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.mutiny.pgclient.PgPool;
import kz.sabyrzhan.DbResource;
import kz.sabyrzhan.clients.DictClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@QuarkusTestResource(DbResource.class)
public class OrdersResourceTest {
    @Inject
    @RestClient
    DictClient dictClient;

    @Inject
    PgPool pgPool;

    @BeforeEach
    public void setUp() {
        pgPool.query("DELETE FROM pos_categories").execute().await().indefinitely();
        pgPool.query("DELETE FROM pos_order_items").execute().await().indefinitely();
        pgPool.query("DELETE FROM pos_orders").execute().await().indefinitely();
        pgPool.query("DELETE FROM pos_products").execute().await().indefinitely();
        pgPool.query("DELETE FROM pos_store_configs").execute().await().indefinitely();
    }
}