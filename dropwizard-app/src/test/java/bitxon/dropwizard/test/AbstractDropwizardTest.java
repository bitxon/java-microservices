package bitxon.dropwizard.test;

import static io.dropwizard.testing.ConfigOverride.config;

import bitxon.dropwizard.DropwizardApplication;
import bitxon.dropwizard.DropwizardConfiguration;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.dropwizard.testing.ConfigOverrideRandomPorts;
import io.dropwizard.testing.DropwizardTestSupport;
import io.dropwizard.testing.ResourceHelpers;
import io.restassured.RestAssured;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;


abstract class AbstractDropwizardTest {

    static PostgreSQLContainer DB = (PostgreSQLContainer) new PostgreSQLContainer(DockerImageName.parse("postgres").withTag("14.4"))
        .withDatabaseName("testdb")
        .withUsername("postgres")
        .withPassword("postgres")
        .withInitScript("sql/db-test-data.sql");
    static GenericContainer WIREMOCK = new GenericContainer("wiremock/wiremock:2.35.0")
        .withExposedPorts(8080)
        .withCopyFileToContainer(MountableFile.forClasspathResource("stubs"), "/home/wiremock")
        .waitingFor(Wait
            .forHttp("/__admin/mappings")
            .withMethod("GET")
            .forStatusCode(200));
    static DropwizardTestSupport<DropwizardConfiguration> APP = new DropwizardTestSupport<>(DropwizardApplication.class,
        ResourceHelpers.resourceFilePath("config-test.yml"),
        ConfigOverrideRandomPorts.randomPorts(),
        config("database.url", DB::getJdbcUrl),
        config("database.user", DB::getUsername),
        config("database.password", DB::getPassword),
        config("exchangeClientConfig.basePath", () -> String.format("http://%s:%s/exchanges?currency=", WIREMOCK.getHost(), WIREMOCK.getMappedPort(8080)))
    );

    static {
        Startables.deepStart(DB, WIREMOCK).join();
        WireMock.configureFor(WIREMOCK.getMappedPort(8080));

        try {
            // This trick helps to spin up application once
            APP.before();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        RestAssured.port = APP.getLocalPort();
    }

}
