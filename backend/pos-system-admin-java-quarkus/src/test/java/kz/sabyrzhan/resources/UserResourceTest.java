package kz.sabyrzhan.resources;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.vertx.mutiny.pgclient.PgPool;
import kz.sabyrzhan.clients.UserClient;
import kz.sabyrzhan.entities.UserEntity;
import kz.sabyrzhan.model.UserRole;
import lombok.SneakyThrows;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;


@QuarkusTest
class UserResourceTest {
    @Inject
    @RestClient
    UserClient userClient;

    @Inject
    PgPool pgPool;

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
        UserEntity newUserRequest = createUser();
        // Create user
        userClient.createUser(newUserRequest);
        // Create user but same email
        newUserRequest.setUsername("username2");

        given()
                .body(newUserRequest)
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
        UserEntity newUserRequest = createUser();
        // Create user
        userClient.createUser(newUserRequest);

        // Create user but same username
        newUserRequest.setEmail("diff@email.com");

        given()
                .body(newUserRequest)
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
    @SneakyThrows
    void deleteUser_success() {
        var newUser = userClient.createUser(createUser());

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/v1/users/" + newUser.getId())
                .then()
                .contentType(equalTo(ContentType.JSON.toString()))
                .statusCode(equalTo(Response.Status.ACCEPTED.getStatusCode()));

        int actualUserCount = pgPool.query("SELECT count(*) c FROM pos_users").execute().await()
                                .indefinitely()
                                .iterator()
                                .next()
                                .getInteger("c");
        assertEquals(0, actualUserCount);
    }

    @Test
    @SneakyThrows
    void deleteUser_successEvenNoUser() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/v1/users/1")
                .then()
                .contentType(equalTo(ContentType.JSON.toString()))
                .statusCode(equalTo(Response.Status.ACCEPTED.getStatusCode()));

        int actualUserCount = pgPool.query("SELECT count(*) c FROM pos_users").execute().await()
                .indefinitely()
                .iterator()
                .next()
                .getInteger("c");
        assertEquals(0, actualUserCount);
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
