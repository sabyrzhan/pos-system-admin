package kz.sabyrzhan.resources;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.vertx.mutiny.pgclient.PgPool;
import kz.sabyrzhan.DbResource;
import kz.sabyrzhan.clients.DictClient;
import kz.sabyrzhan.clients.ProductClient;
import kz.sabyrzhan.entities.CategoryEntity;
import kz.sabyrzhan.entities.ProductEntity;
import kz.sabyrzhan.model.Product;
import kz.sabyrzhan.repositories.ProductRepository;
import lombok.SneakyThrows;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.LinkedList;

import static io.restassured.RestAssured.given;
import static kz.sabyrzhan.repositories.CategoryRepository.CATEGORY_NOT_FOUND_MESSAGE;
import static kz.sabyrzhan.services.ProductService.PRODUCT_NAME_ALREADY_EXISTS;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@QuarkusTestResource(DbResource.class)
class ProductResourceTest {
    @Inject
    @RestClient
    ProductClient productClient;

    @Inject
    @RestClient
    DictClient dictClient;

    @Inject
    PgPool pgPool;

    @BeforeEach
    public void setUp() {
        pgPool.query("DELETE FROM pos_categories").execute().await().indefinitely();
        pgPool.query("DELETE FROM pos_products").execute().await().indefinitely();
    }

    @Test
    public void addProduct_success() {
        var postData = createProduct();

        var response = given()
                .body(postData)
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/products")
                .then()
                .contentType(containsString(ContentType.JSON.toString()))
                .statusCode(equalTo(200))
                .extract().as(ProductEntity.class);

        assertEquals(postData.getName(), response.getName());
        assertEquals(postData.getCategoryId(), response.getCategoryId());
        assertEquals(postData.getPurchasePrice(), response.getPurchasePrice());
        assertEquals(postData.getStock(), response.getStock());
        assertEquals(postData.getDescription(), response.getDescription());
    }

    @Test
    public void addProduct_existsWithName() {
        var postData = productClient.addProduct(createProduct());

        given()
                .body(postData)
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/products")
                .then()
                .contentType(containsString(ContentType.JSON.toString()))
                .statusCode(equalTo(409))
                .and()
                .body("error", equalTo(PRODUCT_NAME_ALREADY_EXISTS));
    }

    @Test
    public void updateProduct_success() {
        var categoryData = dictClient.create(DictionaryResourceTest.createCategory());
        var postData = createProduct();
        postData.setCategoryId(categoryData.getId());
        postData = productClient.addProduct(postData);
        postData.setName(postData.getName() + "_newName");

        given()
                .body(postData)
                .contentType(ContentType.JSON)
                .when()
                .put("/api/v1/products/" + postData.getId())
                .then()
                .contentType(containsString(ContentType.JSON.toString()))
                .statusCode(equalTo(200))
                .and()
                .body("id", equalTo(postData.getId()))
                .and()
                .body("name", equalTo(postData.getName()));
    }

    @Test
    public void updateProduct_sameNameExists() {
        var categoryData1 = DictionaryResourceTest.createCategory();
        categoryData1.setName("Cat1");
        categoryData1 = dictClient.create(categoryData1);
        CategoryEntity categoryData2 = DictionaryResourceTest.createCategory();
        categoryData2.setName("Cat2");
        categoryData2 = dictClient.create(categoryData2);

        var postData1 = createProduct();
        postData1.setCategoryId(categoryData1.getId());
        postData1 = productClient.addProduct(postData1);

        var postData2 = createProduct();
        postData2.setCategoryId(categoryData2.getId());
        postData2.setName(postData2.getName() + "newName");
        postData2 = productClient.addProduct(postData2);
        postData2.setName(postData1.getName());

        given()
                .body(postData2)
                .contentType(ContentType.JSON)
                .when()
                .put("/api/v1/products/" + postData2.getId())
                .then()
                .contentType(containsString(ContentType.JSON.toString()))
                .statusCode(equalTo(409))
                .and()
                .body("error", equalTo(PRODUCT_NAME_ALREADY_EXISTS));
    }

    @Test
    public void updateProduct_categoryNotFound() {
        var categoryData = dictClient.create(DictionaryResourceTest.createCategory());
        var postData = createProduct();
        postData.setCategoryId(categoryData.getId());
        postData = productClient.addProduct(postData);
        postData.setCategoryId(-1);

        given()
                .body(postData)
                .contentType(ContentType.JSON)
                .when()
                .put("/api/v1/products/" + postData.getId())
                .then()
                .contentType(containsString(ContentType.JSON.toString()))
                .statusCode(equalTo(404))
                .and()
                .body("error", equalTo(CATEGORY_NOT_FOUND_MESSAGE));
    }

    @Test
    public void getById_success() {
        var cat = dictClient.create(DictionaryResourceTest.createCategory());
        var requestData = createProduct();
        requestData.setCategoryId(cat.getId());
        var savedData = productClient.addProduct(requestData);

        var response = given()
                .body(savedData)
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/products/" + savedData.getId())
                .then()
                .contentType(containsString(ContentType.JSON.toString()))
                .statusCode(equalTo(200))
                .extract().as(Product.class);

        assertEquals(requestData.getName(), response.getName());
        assertEquals(requestData.getCategoryId(), response.getCategoryId());
        assertEquals(requestData.getPurchasePrice(), response.getPurchasePrice());
        assertEquals(requestData.getStock(), response.getStock());
        assertEquals(requestData.getDescription(), response.getDescription());
    }

    @Test
    public void getById_noEntity() {
        ProductEntity requestData = createProduct();

        given()
                .body(requestData)
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/products/1")
                .then()
                .contentType(containsString(ContentType.JSON.toString()))
                .statusCode(equalTo(404))
                .and()
                .body("error", equalTo(ProductRepository.PRODUCT_NOT_FOUND_MESSAGE));
    }

    @Test
    @SneakyThrows
    public void getProducts_success() {
        var category = dictClient.create(DictionaryResourceTest.createCategory());
        LinkedList<ProductEntity> products = new LinkedList<>();
        for(int i = 1; i <= 10; i++) {
            var product = createProduct();
            product.setName("TestName" + i);
            product.setCategoryId(category.getId());
            product = productClient.addProduct(product);
            products.addFirst(product);
            Thread.sleep(200); // Little delay so sort by created date really will work
        }

        var sizePerPage = 5;
        var result = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/products?page=2&sizePerPage=" + sizePerPage)
                .then()
                .contentType(containsString(ContentType.JSON.toString()))
                .statusCode(equalTo(200))
                .extract().as(Product[].class);

        for(int i = 0; i < sizePerPage; i++) {
            var ex = products.get(i + 5); // second page items
            var re = result[i];
            assertEquals(ex.getId(), re.getId());
            assertEquals(ex.getName(), re.getName());
            assertEquals(category.getName(), re.getCategory());
            assertEquals(ex.getDescription(), re.getDescription());
            assertEquals(ex.getPurchasePrice(), re.getPurchasePrice());
            assertEquals(ex.getSalePrice(), re.getSalePrice());
            assertEquals(ex.getStock(), re.getStock());
            assertEquals(ex.getCreated(), re.getCreated());
        }
    }

    private ProductEntity createProduct() {
        var entity = new ProductEntity();
        entity.setCategoryId(1);
        entity.setName("TestProduct");
        entity.setDescription("TestDescription");
        entity.setPurchasePrice(1.0f);
        entity.setSalePrice(2.0f);
        entity.setStock(10);

        return entity;
    }
}