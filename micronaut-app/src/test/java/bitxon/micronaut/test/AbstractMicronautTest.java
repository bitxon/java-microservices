package bitxon.micronaut.test;

import java.util.Map;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@MicronautTest // default environments is 'test'
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class AbstractMicronautTest implements TestPropertyProvider {

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
        WireMock.configureFor(WIREMOCK.getMappedPort(8080));
    }

    @Inject
    private EmbeddedApplication<?> application;

    @Override
    public Map<String, String> getProperties() {
        return Map.of(
            "datasources.default.url", DB.getJdbcUrl(),
            "datasources.default.username", DB.getUsername(),
            "datasources.default.password", DB.getPassword(),
            "micronaut.http.services.exchange-client.url", String.format("http://%s:%d", WIREMOCK.getHost(), WIREMOCK.getMappedPort(8080))
        );
    }
}
