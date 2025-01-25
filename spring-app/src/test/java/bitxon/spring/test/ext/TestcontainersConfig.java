package bitxon.spring.test.ext;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.MountableFile;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfig {

    @Bean
    public DynamicPropertyRegistrar propertiesOverride(GenericContainer wiremockContainer) {
        return (registry) ->
            registry.add("http.exchange-client.url", () -> httpUrl(wiremockContainer, 8080));

    }

    @Bean
    @ServiceConnection
    public PostgreSQLContainer postgreSQLContainer() {
        return (PostgreSQLContainer) new PostgreSQLContainer("postgres:14.4")
            .withDatabaseName("testdb")
            .withUsername("postgres")
            .withPassword("postgres")
            .withInitScript("sql/db-test-data.sql");
    }

    @Bean
    public GenericContainer wiremockContainer() {
        return new GenericContainer("wiremock/wiremock:3.0.0-1")
            .withExposedPorts(8080)
            .withCopyFileToContainer(MountableFile.forClasspathResource("stubs"), "/home/wiremock")
            .waitingFor(Wait
                .forHttp("/__admin/mappings")
                .withMethod("GET")
                .forStatusCode(200));
    }

    private static String httpUrl(String host, Integer port) {
        return String.format("http://%s:%d", host, port);
    }

    private static String httpUrl(GenericContainer container, Integer originalPort) {
        return httpUrl(container.getHost(), container.getMappedPort(originalPort));
    }
}
