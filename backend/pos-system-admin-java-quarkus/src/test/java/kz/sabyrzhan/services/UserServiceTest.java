package kz.sabyrzhan.services;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.vertx.mutiny.pgclient.PgPool;
import kz.sabyrzhan.entities.UserEntity;
import kz.sabyrzhan.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


@QuarkusTest
class UserServiceTest {
    @Inject
    private PgPool pgPool;

    @BeforeEach
    public void setUp() {
        pgPool.query("DELETE FROM pos_users").execute().await().indefinitely();
    }

    @Test
    void createUser_success() {
        UserEntity postData = new UserEntity();
        postData.setUsername("test");
        postData.setEmail("test@test.com");
        postData.setPassword("pass");
        postData.setRole(UserRole.USER.name());

        given()
                .body(postData)
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/users")
                .then()
                .contentType(equalTo(ContentType.JSON.toString()))
                .statusCode(equalTo(200))
                .and()
                .body("username", equalTo(postData.getUsername()));
    }

    @Test
    void createUser_sameEmail() {
        UserEntity user1 = createUser();

        UserEntity user1SameEmail = createUser();
        user1SameEmail.setUsername("username2");

        given()
                .body(user1)
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/users")
                .then()
                .contentType(equalTo(ContentType.JSON.toString()))
                .statusCode(equalTo(200))
                .and()
                .body("username", equalTo(user1.getUsername()));

        given()
                .body(user1SameEmail)
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/users")
                .then()
                .contentType(equalTo(ContentType.JSON.toString()))
                .statusCode(equalTo(409))
                .and()
                .body("error", equalTo("User already exists"));
    }

    @Test
    void createUser_sameUsername() {
        UserEntity user1 = createUser();

        UserEntity user1SameUsername = createUser();
        user1SameUsername.setEmail("diff@email.com");

        given()
                .body(user1)
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/users")
                .then()
                .contentType(equalTo(ContentType.JSON.toString()))
                .statusCode(equalTo(200))
                .and()
                .body("username", equalTo(user1.getUsername()));

        given()
                .body(user1SameUsername)
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/users")
                .then()
                .contentType(equalTo(ContentType.JSON.toString()))
                .statusCode(equalTo(409))
                .and()
                .body("error", equalTo("User already exists"));
    }

    private UserEntity createUser() {
        UserEntity postData = new UserEntity();
        postData.setUsername("test");
        postData.setEmail("test@test.com");
        postData.setPassword("pass");
        postData.setRole(UserRole.USER.name());

        return postData;
    }
}
