package app.TaskHub;

import app.TaskHub.dto.req.LoginRequest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.UUID;

import static app.TaskHub.api.UserApi.USER_BASE_URL;
import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class TaskHubApplicationTests {

	@LocalServerPort
	protected Integer port;

	public static final String ADMIN = "ADMIN";
	public static final String USER = "USER";
	public static final String USER_USERNAME = "user";

	protected RequestSpecification publicRequestSpecification;
	protected RequestSpecification userRequestSpecification;
	protected RequestSpecification adminRequestSpecification;

	@DynamicPropertySource
	static void initialize(final DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", () -> "jdbc:postgresql://localhost:5432/TaskHub");
		registry.add("spring.datasource.username", () -> "postgres");
		registry.add("spring.datasource.password", () -> "01032002ama");
	}

	@BeforeEach
	protected void setUp() {
		publicRequestSpecification =
				new RequestSpecBuilder()
						.addFilter(new RequestLoggingFilter())
						.addFilter(new ResponseLoggingFilter())
						.setPort(port)
						.setContentType(ContentType.JSON)
						.build();

//		userRequestSpecification =
//				new RequestSpecBuilder()
//						.addFilter(new RequestLoggingFilter())
//						.addFilter(new ResponseLoggingFilter())
//						.addHeader("Authorization", getUserId(USER))  // ← userId вместо token
//						.setPort(port)
//						.setContentType(ContentType.JSON)
//						.build();
//
//		adminRequestSpecification =
//				new RequestSpecBuilder()
//						.addFilter(new RequestLoggingFilter())
//						.addFilter(new ResponseLoggingFilter())
//						.addHeader("Authorization", getUserId(ADMIN))  // ← userId вместо token
//						.setPort(port)
//						.setContentType(ContentType.JSON)
//						.build();
	}

//	private String getUserId(String role) {
//		LoginRequest adminRequest = new LoginRequest("admin", "admin");
//		LoginRequest userRequest = new LoginRequest(USER_USERNAME, USER_USERNAME);
//
//		String userIdStr = given(publicRequestSpecification)
//				.body(role.equals(ADMIN) ? adminRequest : userRequest)
//				.when()
//				.post(USER_BASE_URL + "/login")
//				.then()
//				.statusCode(HttpStatus.OK.value())
//				.extract()
//				.path("id")
//				.toString();  // ← конвертируй в String
//
//		return userIdStr;
//	}
}