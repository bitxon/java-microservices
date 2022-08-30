package bitxon.spring.test;

import bitxon.spring.SpringAppApplication;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@ActiveProfiles("test")
@SpringBootTest(classes = SpringAppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AbstractSpringTest {

    private static final PostgreSQLContainer DB;
    private static final WireMockServer WIREMOCK;

    static {
        DB = (PostgreSQLContainer) new PostgreSQLContainer(DockerImageName.parse("postgres").withTag("14.4"))
            .withDatabaseName("testdb")
            .withUsername("postgres")
            .withPassword("postgres")
            .withInitScript("sql/db-test-data.sql");
        DB.start();

        WIREMOCK = new WireMockServer(WireMockConfiguration.options()
            .usingFilesUnderClasspath("stubs") // Loading stubs from common-wiremock
            .dynamicPort()
        );
        WIREMOCK.start();
        WireMock.configureFor(WIREMOCK.port());
    }

    @DynamicPropertySource
    static void dynamicProperty(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", DB::getJdbcUrl);
        registry.add("spring.datasource.username", DB::getUsername);
        registry.add("spring.datasource.password", DB::getPassword);
        registry.add("wiremock.server.port", WIREMOCK::port);
    }

    @LocalServerPort
    private Integer port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

}
