package app.TaskHub.api;

import app.TaskHub.TaskHubApplicationTests;
import app.TaskHub.dto.req.LoginRequest;
import app.TaskHub.dto.req.user.UserRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

import static app.TaskHub.api.UserApi.USER_BASE_URL;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserApiTest extends TaskHubApplicationTests {

//    @Test
//    void login_shouldWork() {
//        given(publicRequestSpecification)
//                .body(new LoginRequest(USER_USERNAME, USER_USERNAME))
//                .when()
//                .post(USER_BASE_URL + "/login")
//                .then()
//                .statusCode(HttpStatus.OK.value());
//    }

    @Test
    void get_shouldReturnAllUsers(){
        given(publicRequestSpecification)
                .when()
                .get(USER_BASE_URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(greaterThanOrEqualTo(2)))
                .body("[0].id", notNullValue())
                .body("[0].username", notNullValue());
    }

    @Test
    void getById_shouldReturnUser(){
        String userId = given(publicRequestSpecification)
                .when()
                .get(USER_BASE_URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .path("[0].id")
                .toString();

        given(publicRequestSpecification)
                .when()
                .get(USER_BASE_URL + "/" + userId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(userId))
                .body("username", notNullValue());
    }

    @Test
    void create_shouldCreateNewUser(){
        UUID roleId = UUID.randomUUID();
        UserRequest request = new UserRequest(
                "Test",
                "User",
                "testUser",
                "password123",
                "1234567890",
                "test@test.com",
                List.of(roleId)
        );

        given(publicRequestSpecification)
                .body(request)
                .when()
                .post(USER_BASE_URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("username", equalTo("testUser"))
                .body("email", equalTo("test@test.com"));
    }

    @Test
    void update_shouldUpdateUser(){
        // 1. Получаем ID существующего пользователя (например, user)
        String userId = given(publicRequestSpecification)
                .body(new LoginRequest(USER_USERNAME, USER_USERNAME))
                .when()
                .post(USER_BASE_URL + "/login")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .path("id")
                .toString();

        // 2. Получаем ID роли USER
        UUID roleId = UUID.randomUUID();

        // 3. Создаём запрос на обновление
        UserRequest updateRequest = new UserRequest(
                "Updated",
                "Name",
                "updateduser",
                "newpassword",
                "987654321",
                "updated@test.com",
                List.of(roleId)
        );

        // 4. Отправляем PUT запрос
        given(publicRequestSpecification)
                .body(updateRequest)
                .when()
                .put(USER_BASE_URL + "/" + userId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(userId))
                .body("firstName", equalTo("Updated"))
                .body("lastName", equalTo("Name"))
                .body("username", equalTo("updateduser"))
                .body("email", equalTo("updated@test.com"))
                .body("phoneNumber", equalTo("987654321"));
    }
}
