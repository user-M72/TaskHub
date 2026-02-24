package app.TaskHub.api;

import app.TaskHub.TaskHubApplicationTests;
import app.TaskHub.dto.req.LoginRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;

import static app.TaskHub.api.UserApi.USER_BASE_URL;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;

public class UserApiTest extends TaskHubApplicationTests {

    @Test
    void login_shouldWork() {
        given(publicRequestSpecification)
                .body(new LoginRequest(USER_USERNAME, USER_USERNAME))
                .when()
                .post(USER_BASE_URL + "/login")
                .then()
                .statusCode(HttpStatus.OK.value());
//                .body("$", not(hasKey("token")));
    }

}
