package kz.sabyrzhan.resources;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.vertx.mutiny.pgclient.PgPool;
import kz.sabyrzhan.clients.DictClient;
import kz.sabyrzhan.entities.CategoryEntity;
import kz.sabyrzhan.services.CategoryService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class DictionaryResourceTest {
    @Inject
    @RestClient
    DictClient dictClient;

    @Inject
    PgPool pgPool;

    @BeforeEach
    public void setUp() {
        pgPool.query("DELETE FROM pos_categories").execute().await().indefinitely();
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
                .body("error", equalTo(CategoryService.CATEGORY_NOT_FOUND_MESSAGE));
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

        assertEquals(result.getError(), CategoryService.CATEGORY_NOT_FOUND_MESSAGE);
    }

    private CategoryEntity createCategory() {
        CategoryEntity entity = new CategoryEntity();
        entity.setName("testCategory");
        return entity;
    }
}