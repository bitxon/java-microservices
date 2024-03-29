package bitxon.micronaut.test;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.micronaut.http.server.netty.NettyHttpServer;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import io.restassured.RestAssured;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.util.Map;

@MicronautTest // default environments is 'test'
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class AbstractMicronautTest implements TestPropertyProvider {

    static PostgreSQLContainer DB = (PostgreSQLContainer) new PostgreSQLContainer(DockerImageName.parse("postgres").withTag("14.4"))
        .withDatabaseName("testdb")
        .withUsername("postgres")
        .withPassword("postgres")
        .withInitScript("sql/db-test-data.sql");
    static GenericContainer WIREMOCK = new GenericContainer("wiremock/wiremock:3.0.0-1")
        .withExposedPorts(8080)
        .withCopyFileToContainer(MountableFile.forClasspathResource("stubs"), "/home/wiremock")
        .waitingFor(Wait
            .forHttp("/__admin/mappings")
            .withMethod("GET")
            .forStatusCode(200));

    static {
        Startables.deepStart(DB, WIREMOCK).join();
    }

    @Inject
    private EmbeddedApplication<?> application;

    @Override
    public Map<String, String> getProperties() {
        return Map.of(
            "micronaut.server.port", "-1",
            "endpoints.all.port", "-1",
            "datasources.default.url", DB.getJdbcUrl(),
            "datasources.default.username", DB.getUsername(),
            "datasources.default.password", DB.getPassword(),
            "micronaut.http.services.exchange-client.url", String.format("http://%s:%d", WIREMOCK.getHost(), WIREMOCK.getMappedPort(8080))
        );
    }

    @BeforeEach
    public void setUp() {
        RestAssured.port = ((NettyHttpServer) application).getPort();;
        WireMock.configureFor(WIREMOCK.getHost(), WIREMOCK.getMappedPort(8080));
        // Native micronaut way to do that is to inject `RequestSpecification spec` into test
        // @Test
        // void testMethod(RequestSpecification spec) {
    }
}
