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
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@MicronautTest // default environments is 'test'
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class AbstractMicronautTest implements TestPropertyProvider {

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

    @Inject
    private EmbeddedApplication<?> application;

    @Override
    public Map<String, String> getProperties() {
        return Map.of(
            "datasources.default.url", DB.getJdbcUrl(),
            "datasources.default.username", DB.getUsername(),
            "datasources.default.password", DB.getPassword(),
            "micronaut.http.services.exchange-client.url", WIREMOCK.baseUrl()
        );
    }
}
