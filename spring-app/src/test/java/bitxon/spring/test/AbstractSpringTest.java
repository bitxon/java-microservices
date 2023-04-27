package bitxon.spring.test;

import bitxon.spring.SpringAppApplication;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

@ActiveProfiles("test")
@SpringBootTest(classes = SpringAppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AbstractSpringTest {

    static PostgreSQLContainer DB = (PostgreSQLContainer) new PostgreSQLContainer(DockerImageName.parse("postgres").withTag("14.4"))
        .withDatabaseName("testdb")
        .withUsername("postgres")
        .withPassword("postgres")
        .withInitScript("sql/db-test-data.sql");
    static GenericContainer WIREMOCK = new GenericContainer("wiremock/wiremock:2.35.0")
        .withExposedPorts(8080)
        .withClasspathResourceMapping("stubs", "/home/wiremock", BindMode.READ_ONLY)
        .waitingFor(Wait
            .forHttp("/__admin/mappings")
            .withMethod("GET")
            .forStatusCode(200));

    static {
        Startables.deepStart(DB, WIREMOCK).join();
    }

    @DynamicPropertySource
    static void dynamicProperty(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", DB::getJdbcUrl);
        registry.add("spring.datasource.username", DB::getUsername);
        registry.add("spring.datasource.password", DB::getPassword);
        registry.add("wiremock.server.port", () -> WIREMOCK.getMappedPort(8080));
    }

    @LocalServerPort
    private Integer port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        WireMock.configureFor(WIREMOCK.getMappedPort(8080));
    }

}
