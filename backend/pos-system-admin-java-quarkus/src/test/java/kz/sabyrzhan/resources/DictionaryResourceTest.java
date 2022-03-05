package kz.sabyrzhan.resources;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.vertx.mutiny.pgclient.PgPool;
import kz.sabyrzhan.DbResource;
import kz.sabyrzhan.clients.DictClient;
import kz.sabyrzhan.entities.CategoryEntity;
import kz.sabyrzhan.entities.StoreConfigEntity;
import kz.sabyrzhan.model.ConfigKey;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.time.temporal.ChronoUnit;

import static io.restassured.RestAssured.given;
import static kz.sabyrzhan.repositories.CategoryRepository.CATEGORY_NOT_FOUND_MESSAGE;
import static kz.sabyrzhan.services.CategoryService.CATEGORY_NAME_ALREADY_EXISTS;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@QuarkusTestResource(DbResource.class)
class DictionaryResourceTest {
    @Inject
    @RestClient
    DictClient dictClient;

    @Inject
    PgPool pgPool;

    @BeforeEach
    public void setUp() {
        pgPool.query("DELETE FROM pos_categories").execute().await().indefinitely();
        pgPool.query("DELETE FROM pos_store_configs").execute().await().indefinitely();
    }

    @Test
    void createCategory_success() {
        CategoryEntity postData = createCategory();

        given()
                .body(postData)
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/dict/categories")
                .then()
                .contentType(containsString(ContentType.JSON.toString()))
                .statusCode(equalTo(200))
                .and()
                .body("name", equalTo(postData.getName()));
    }

    @Test
    void createCategory_withSameNameExists() {
        CategoryEntity newCategory = createCategory();
        dictClient.create(newCategory);

        given()
                .body(newCategory)
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/dict/categories")
                .then()
                .contentType(equalTo(ContentType.JSON.toString()))
                .statusCode(equalTo(409))
                .and()
                .body("error", equalTo("Category with this name already exists"));
    }

    @Test
    void updateCategory_success() {
        CategoryEntity newCategory = dictClient.create(createCategory());

        newCategory.setName("newName");

        given()
                .body(newCategory)
                .contentType(ContentType.JSON)
                .when()
                .put("/api/v1/dict/categories/" + newCategory.getId())
                .then()
                .contentType(equalTo(ContentType.JSON.toString()))
                .statusCode(equalTo(200))
                .and()
                .body("name", equalTo(newCategory.getName()));
    }

    @Test
    void updateCategory_notFound() {
        CategoryEntity updateCategory = createCategory();

        given()
                .body(updateCategory)
                .contentType(ContentType.JSON)
                .when()
                .put("/api/v1/dict/categories/1")
                .then()
                .contentType(equalTo(ContentType.JSON.toString()))
                .statusCode(equalTo(404))
                .and()
                .body("error", equalTo(CATEGORY_NOT_FOUND_MESSAGE));
    }

    @Test
    void updateCategory_sameName() {
        CategoryEntity cat1 = createCategory();
        cat1.setName("Cat1");
        cat1 = dictClient.create(cat1);

        CategoryEntity cat2 = createCategory();
        cat2.setName("Cat2");
        cat2 = dictClient.create(cat2);
        cat2.setName(cat1.getName());

        given()
                .body(cat2)
                .contentType(ContentType.JSON)
                .when()
                .put("/api/v1/dict/categories/" + cat2.getId())
                .then()
                .contentType(equalTo(ContentType.JSON.toString()))
                .statusCode(equalTo(409))
                .and()
                .body("error", equalTo(CATEGORY_NAME_ALREADY_EXISTS));
    }

    @Test
    public void getCategoriesList_success() {
        CategoryEntity created = dictClient.create(createCategory());

        CategoryEntity[] result = given()
                                    .contentType(ContentType.JSON)
                                    .when()
                                    .get("/api/v1/dict/categories")
                                    .then()
                                    .contentType(containsString(ContentType.JSON.toString()))
                                    .statusCode(equalTo(200))
                                    .and()
                                    .extract().as(CategoryEntity[].class);

        assertEquals(1, result.length);
        assertEquals(created.getId(), result[0].getId());
        assertEquals(created.getName(), result[0].getName());
    }

    @Test
    public void getCategoryById_success() {
        CategoryEntity created = dictClient.create(createCategory());

        CategoryEntity result = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/dict/categories/" + created.getId())
                .then()
                .contentType(containsString(ContentType.JSON.toString()))
                .statusCode(equalTo(200))
                .and()
                .extract().as(CategoryEntity.class);

        assertEquals(created.getId(), result.getId());
        assertEquals(created.getName(), result.getName());
    }

    @Test
    public void getCategoryById_notFound() {
        ErrorResponse result = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/dict/categories/1")
                .then()
                .contentType(containsString(ContentType.JSON.toString()))
                .statusCode(equalTo(404))
                .and()
                .extract().as(ErrorResponse.class);

        assertEquals(result.getError(), CATEGORY_NOT_FOUND_MESSAGE);
    }

    @Test
    void createConfig_success() {
        StoreConfigEntity postData = createConfig();

        given()
                .body(postData)
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/dict/configs")
                .then()
                .contentType(containsString(ContentType.JSON.toString()))
                .statusCode(equalTo(200));

        StoreConfigEntity result = dictClient.getByConfigKey(postData.getConfigKey());
        assertNotNull(result);
        assertTrue(result.getId() > 0);
        assertEquals(postData.getConfigKey(), result.getConfigKey());
        assertEquals(postData.getConfigValue(), result.getConfigValue());
        assertEquals(postData.getCreated().truncatedTo(ChronoUnit.SECONDS), result.getCreated().truncatedTo(ChronoUnit.SECONDS));
        assertEquals(postData.getUpdated().truncatedTo(ChronoUnit.SECONDS), result.getUpdated().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    void createConfig_successGetByKey() {
        StoreConfigEntity expected = dictClient.create(createConfig());

        StoreConfigEntity result = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/dict/configs/" + expected.getConfigKey())
                .then()
                .contentType(containsString(ContentType.JSON.toString()))
                .statusCode(equalTo(200))
                .and()
                .extract().body().as(StoreConfigEntity.class);

        assertNotNull(result);
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getConfigKey(), result.getConfigKey());
        assertEquals(expected.getConfigValue(), result.getConfigValue());
        assertEquals(expected.getCreated().truncatedTo(ChronoUnit.SECONDS), result.getCreated().truncatedTo(ChronoUnit.SECONDS));
        assertEquals(expected.getUpdated().truncatedTo(ChronoUnit.SECONDS), result.getUpdated().truncatedTo(ChronoUnit.SECONDS));
    }

    public static CategoryEntity createCategory() {
        CategoryEntity entity = new CategoryEntity();
        entity.setName("testCategory");
        return entity;
    }

    public static StoreConfigEntity createConfig() {
        StoreConfigEntity entity = new StoreConfigEntity();
        entity.setConfigKey(ConfigKey.TAX_PERCENT);
        entity.setConfigValue("5");

        return entity;
    }
}