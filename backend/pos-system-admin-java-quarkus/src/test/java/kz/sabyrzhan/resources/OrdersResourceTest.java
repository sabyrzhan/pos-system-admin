package kz.sabyrzhan.resources;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.vertx.mutiny.pgclient.PgPool;
import kz.sabyrzhan.DbResource;
import kz.sabyrzhan.clients.DictClient;
import kz.sabyrzhan.clients.ProductClient;
import kz.sabyrzhan.entities.*;
import kz.sabyrzhan.model.ConfigKey;
import kz.sabyrzhan.model.PaymentType;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@QuarkusTestResource(DbResource.class)
class OrdersResourceTest {
    @Inject
    @RestClient
    DictClient dictClient;

    @Inject
    @RestClient
    ProductClient productClient;

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
    public void createOrder_success() {
        var orderRequest = createOrder();
        Map<Integer, OrderItemEntity> requestItemMap = orderRequest.getItems().stream().collect(Collectors.toMap(OrderItemEntity::getProductId, Function.identity()));

        var response = given()
                .body(orderRequest)
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/orders")
                .then()
                .contentType(containsString(ContentType.JSON.toString()))
                .statusCode(equalTo(200))
                .and()
                .extract().body().as(OrderEntity.class);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(orderRequest.getPaid(), response.getPaid());
        assertEquals(orderRequest.getTax(), response.getTax());
        assertEquals(orderRequest.getDue(), response.getDue());
        assertEquals(orderRequest.getCustomerName(), response.getCustomerName());
        assertEquals(orderRequest.getDiscount(), response.getDiscount());
        assertEquals(orderRequest.getPaymentType(), response.getPaymentType());
        assertEquals(orderRequest.getSubtotal(), response.getSubtotal());
        assertEquals(orderRequest.getTotal(), response.getTotal());
        assertNotNull(response.getItems());
        assertEquals(requestItemMap.size(), response.getItems().size());
        Map<Integer, OrderItemEntity> responseItemMap = response.getItems().stream().collect(Collectors.toMap(OrderItemEntity::getProductId, Function.identity()));
        for(int productId: responseItemMap.keySet()) {
            var expectedItem = requestItemMap.get(productId);
            var resultItem = responseItemMap.get(productId);
            assertTrue(resultItem.getId() > 0);
            assertEquals(response.getId(), resultItem.getOrderId());
            assertEquals(resultItem.getProductId(), expectedItem.getProductId());
            assertEquals(resultItem.getProductName(), expectedItem.getProductName());
            assertEquals(resultItem.getQuantity(), expectedItem.getQuantity());
            assertEquals(resultItem.getPrice(), expectedItem.getPrice());
        }
        int expectedProduct1Stock = product1.getStock() - 1;
        int expectedProduct2Stock = product2.getStock() - 1;
        int expectedProduct3Stock = product3.getStock() - 1;
        var updatedProduct1 = productClient.getById(product1.getId());
        var updatedProduct2 = productClient.getById(product2.getId());
        var updatedProduct3 = productClient.getById(product3.getId());
        assertEquals(expectedProduct1Stock, updatedProduct1.getStock());
        assertEquals(expectedProduct2Stock, updatedProduct2.getStock());
        assertEquals(expectedProduct3Stock, updatedProduct3.getStock());
    }

    @Test
    public void createOrder_invalidTotal() {
        var orderRequest = createOrder();
        orderRequest.setTotal(500);

        given()
                .body(orderRequest)
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/orders")
                .then()
                .contentType(containsString(ContentType.JSON.toString()))
                .statusCode(equalTo(400))
                .and()
                .body("error", equalTo("Total is invalid"));

        var updatedProduct1 = productClient.getById(product1.getId());
        var updatedProduct2 = productClient.getById(product2.getId());
        var updatedProduct3 = productClient.getById(product3.getId());
        assertEquals(product1.getStock(), updatedProduct1.getStock());
        assertEquals(product2.getStock(), updatedProduct2.getStock());
        assertEquals(product3.getStock(), updatedProduct3.getStock());
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