package bitxon.dropwizard;


import static io.dropwizard.testing.ConfigOverride.config;

import javax.ws.rs.client.Client;


import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.DropwizardTestSupport;
import io.dropwizard.testing.ResourceHelpers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;


public abstract class AbstractDropwizardTest {

    private static final PostgreSQLContainer DB;
    public static final WireMockServer WIREMOCK;
    private static final DropwizardTestSupport<DropwizardConfiguration> APP;
    private static final Client CLIENT;

    static {
        DB = (PostgreSQLContainer) new PostgreSQLContainer(DockerImageName.parse("postgres").withTag("9.6.12"))
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

        CLIENT = new JerseyClientBuilder(APP.getEnvironment()).build("test-client");

    }

    protected Client client() {
        return CLIENT;
    }

    protected int appLocalPort() {
        return APP.getLocalPort();
    }

}
