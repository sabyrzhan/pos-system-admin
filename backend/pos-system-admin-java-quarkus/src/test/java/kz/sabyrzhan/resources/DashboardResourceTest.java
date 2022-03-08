package kz.sabyrzhan.resources;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.vertx.mutiny.pgclient.PgPool;
import kz.sabyrzhan.DbResource;
import kz.sabyrzhan.clients.DictClient;
import kz.sabyrzhan.clients.OrderClient;
import kz.sabyrzhan.clients.ProductClient;
import kz.sabyrzhan.entities.*;
import kz.sabyrzhan.model.ConfigKey;
import kz.sabyrzhan.model.DashboardInfo;
import kz.sabyrzhan.model.PaymentType;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@QuarkusTestResource(DbResource.class)
class DashboardResourceTest {
    @Inject
    @RestClient
    DictClient dictClient;

    @Inject
    @RestClient
    ProductClient productClient;

    @Inject
    @RestClient
    OrderClient orderClient;

    @Inject
    PgPool pgPool;

    StoreConfigEntity taxConfig;
    CategoryEntity category1, category2, category3;
    ProductEntity product1, product2, product3;

    @BeforeEach
    public void setUp() {
        pgPool.query("DELETE FROM pos_categories").execute().await().indefinitely();
        pgPool.query("DELETE FROM pos_order_items").execute().await().indefinitely();
        pgPool.query("DELETE FROM pos_orders").execute().await().indefinitely();
        pgPool.query("DELETE FROM pos_products").execute().await().indefinitely();
        pgPool.query("DELETE FROM pos_store_configs").execute().await().indefinitely();

        var category1Request = DictionaryResourceTest.createCategory();
        category1Request.setName("TestCategory1");
        var category2Request = DictionaryResourceTest.createCategory();
        category2Request.setName("TestCategory2");
        var category3Request = DictionaryResourceTest.createCategory();
        category3Request.setName("TestCategory3");

        category1 = dictClient.create(category1Request);
        category2 = dictClient.create(category2Request);
        category3 = dictClient.create(category3Request);

        var product1Request = ProductResourceTest.createProduct();
        product1Request.setCategoryId(category1.getId());
        product1Request.setStock(10);
        product1Request.setName("Product1");
        product1Request.setDescription("Product1 desc");
        product1Request.setSalePrice(100);

        var product2Request = ProductResourceTest.createProduct();
        product2Request.setCategoryId(category2.getId());
        product2Request.setStock(20);
        product2Request.setName("Product2");
        product2Request.setDescription("Product2 desc");
        product2Request.setSalePrice(200);

        var product3Request = ProductResourceTest.createProduct();
        product3Request.setCategoryId(category2.getId());
        product3Request.setStock(30);
        product3Request.setName("Product3");
        product3Request.setDescription("Product3 desc");
        product3Request.setSalePrice(300);

        product1 = productClient.addProduct(product1Request);
        product2 = productClient.addProduct(product2Request);
        product3 = productClient.addProduct(product3Request);

        var taxRequest = DictionaryResourceTest.createConfig();
        taxRequest.setConfigKey(ConfigKey.TAX_PERCENT);
        taxRequest.setConfigValue("5");
        taxConfig = dictClient.create(taxRequest);
    }

    @Test
    void getInfo_success() {
        var prod1Stock = product1.getStock();
        var prod2Stock = product2.getStock();
        var prod3Stock = product3.getStock();
        var createdOrder = orderClient.create(createOrder());
        var cancelledOrder = orderClient.create(createOrder());
        cancelledOrder = orderClient.cancel(cancelledOrder.getId());
        var inProgressOrder = orderClient.create(createOrder());
        orderClient.startOrder(inProgressOrder.getId());
        var doneOrder = orderClient.create(createOrder());
        orderClient.finishOrder(doneOrder.getId());

        var response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/dashboard/info")
                .then()
                .contentType(containsString(ContentType.JSON.toString()))
                .statusCode(equalTo(200))
                .extract().body().as(DashboardInfo.class);

        assertNotNull(response);
        assertEquals(1, response.getTotalNewOrders());
        assertEquals(1, response.getTotalInProgressOrders());
        assertEquals(1, response.getTotalProcessedOrders());
        assertEquals(1, response.getTotalCancelledOrders());
        assertEquals(0, response.getTotalReturnedOrders());
        assertEquals(3, response.getTotalProducts());
        assertNotNull(response.getTopProducts());
        assertEquals(3, response.getTopProducts().size());
    }

    public OrderEntity createOrder() {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setPaid(800);
        orderEntity.setTax(30);
        orderEntity.setDue(190);
        orderEntity.setCustomerName("CustomerName");
        orderEntity.setDiscount(20);
        orderEntity.setPaymentType(PaymentType.CREDIT);
        orderEntity.setSubtotal(600);
        orderEntity.setTotal(610);

        OrderItemEntity item1 = new OrderItemEntity();
        item1.setProductId(product1.getId());
        item1.setProductName(product1.getName());
        item1.setPrice(product1.getSalePrice());
        item1.setQuantity(1);

        OrderItemEntity item2 = new OrderItemEntity();
        item2.setProductId(product2.getId());
        item2.setProductName(product2.getName());
        item2.setPrice(product2.getSalePrice());
        item2.setQuantity(1);

        OrderItemEntity item3 = new OrderItemEntity();
        item3.setProductId(product3.getId());
        item3.setProductName(product3.getName());
        item3.setPrice(product3.getSalePrice());
        item3.setQuantity(1);

        orderEntity.getItems().add(item1);
        orderEntity.getItems().add(item2);
        orderEntity.getItems().add(item3);

        return orderEntity;
    }
}