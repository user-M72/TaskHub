package app.TaskHub;

import app.TaskHub.dto.req.LoginRequest;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

import java.util.Objects;

import static app.TaskHub.api.UserApi.USER_BASE_URL;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseBody;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@EnableWireMock(@ConfigureWireMock(filesUnderDirectory = "mappings"))
@EnableConfigurationProperties
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public abstract class TaskHubApplicationTests {
	private static final MockServerContainer MOCK_SERVER_CONTAINER;
	private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER;

	@LocalServerPort
	private Integer port;

	@DynamicPropertySource
	static void overrideProps(DynamicPropertyRegistry registry) {
		// override Feign base URL to WireMock
		registry.add("application.sms-service.base-url", () -> "http://localhost:9977");
	}

	@RegisterExtension
	static WireMockExtension wireMockServer =
			WireMockExtension.newInstance()
					.options(
							WireMockConfiguration.wireMockConfig().port(9977).notifier(new ConsoleNotifier(true)))
					.build();

	static {
		// https://hub.docker.com/_/postgres/tags
		POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:14.5");
		POSTGRESQL_CONTAINER.withInitScript("create-core-schema.sql");
		POSTGRESQL_CONTAINER.start();

		// https://hub.docker.com/r/jamesdbloom/mockserver/tags
		MOCK_SERVER_CONTAINER =
				new MockServerContainer(DockerImageName.parse("jamesdbloom/mockserver:mockserver-5.13.2"));
		MOCK_SERVER_CONTAINER.start();
	}

	@DynamicPropertySource
	static void initialize(final DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", TaskHubApplicationTests::postgresJdbcUrl);
		registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
		registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);

		registry.add("spring.liquibase.url", TaskHubApplicationTests::postgresUrl);
		registry.add("spring.liquibase.user", POSTGRESQL_CONTAINER::getUsername);
		registry.add("spring.liquibase.password", POSTGRESQL_CONTAINER::getPassword);

		registry.add("eureka.client.service-url.defaultZone", MOCK_SERVER_CONTAINER::getEndpoint);
		registry.add("application.TsdkHub.url", MOCK_SERVER_CONTAINER::getEndpoint);
		registry.add("spring.liquibase.change-log", TaskHubApplicationTests::changeLog);
	}

	private static String postgresUrl() {
		return String.format("%s", POSTGRESQL_CONTAINER.getJdbcUrl());
	}

	private static String changeLog() {
		return String.format("%s", "classpath:/db.migration/db.changelog-master.xml");
	}

	private static String postgresJdbcUrl() {
		return String.format(
				"jdbc:postgresql://%s:%s/%s",
				POSTGRESQL_CONTAINER.getHost(),
				POSTGRESQL_CONTAINER.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
				POSTGRESQL_CONTAINER.getDatabaseName());
	}

	public static final String ADMIN = "ADMIN";
	public static final String USER = "USER";

	protected RequestSpecification publicRequestSpecification;
	protected RequestSpecification userRequestSpecification;
	protected RequestSpecification adminRequestSpecification;

	public static final String USER_USERNAME = "user";

	@BeforeEach
	protected void setUp(RestDocumentationContextProvider restDocumentation) {

		publicRequestSpecification =
				new RequestSpecBuilder()
						.addFilter(
								documentationConfiguration(restDocumentation)
										.operationPreprocessors()
										.withRequestDefaults(prettyPrint())
										.withResponseDefaults(prettyPrint()))
						.addFilter(new RequestLoggingFilter())
						.addFilter(new ResponseLoggingFilter())
						.setPort(port)
						.setContentType(ContentType.JSON)
						.build();

		System.out.println(publicRequestSpecification.toString());


		userRequestSpecification =
				new RequestSpecBuilder()
						.addFilter(
								documentationConfiguration(restDocumentation)
										.operationPreprocessors()
										.withRequestDefaults(prettyPrint())
										.withResponseDefaults(prettyPrint()))
						.addFilter(new RequestLoggingFilter())
						.addFilter(new ResponseLoggingFilter())
						.addHeader("Authorization", getToken(USER))
						.setPort(port)
						.setContentType(ContentType.JSON)
						.build();

		adminRequestSpecification =
				new RequestSpecBuilder()
						.addFilter(
								documentationConfiguration(restDocumentation)
										.operationPreprocessors()
										.withRequestDefaults(prettyPrint())
										.withResponseDefaults(prettyPrint()))
						.addFilter(new RequestLoggingFilter())
						.addFilter(new ResponseLoggingFilter())
						.addHeader("Authorization", getToken(ADMIN))
						.setPort(port)
						.setContentType(ContentType.JSON)
						.build();
	}

	private String getToken(String role) {
		LoginRequest adminRequest = new LoginRequest("admin", "admin");
		LoginRequest userRequest = new LoginRequest(USER_USERNAME, USER_USERNAME);

		return "Bearer "
				+ given(publicRequestSpecification)
				.filter(document("{method-name}", responseBody()))
				.body(Objects.equals(role, ADMIN) ? adminRequest : userRequest)
				.when()
				.post(USER_BASE_URL + "/login")
				.then()
				.statusCode(HttpStatus.OK.value())
				.extract()
				.path("token")
				.toString();
	}
}

