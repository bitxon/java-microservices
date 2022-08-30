package bitxon.dropwizard.test;

import static io.dropwizard.testing.ConfigOverride.config;

import bitxon.dropwizard.DropwizardApplication;
import bitxon.dropwizard.DropwizardConfiguration;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.dropwizard.testing.DropwizardTestSupport;
import io.dropwizard.testing.ResourceHelpers;
import io.restassured.RestAssured;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;


abstract class AbstractDropwizardTest {

    private static final PostgreSQLContainer DB;
    public static final WireMockServer WIREMOCK;
    private static final DropwizardTestSupport<DropwizardConfiguration> APP;

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

        APP = new DropwizardTestSupport<>(DropwizardApplication.class,
            ResourceHelpers.resourceFilePath("config-test.yml"),
            //config("server.applicationConnectors[0].port", "0"),
            config("database.url", DB.getJdbcUrl()),
            config("database.user", DB.getUsername()),
            config("database.password", DB.getPassword()),
            config("exchangeClientConfig.basePath", WIREMOCK.baseUrl() + "/exchanges?currency=")
        );
        try {
            // This trick helps to spin up application once
            APP.before();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        RestAssured.port = APP.getLocalPort();
    }

}
