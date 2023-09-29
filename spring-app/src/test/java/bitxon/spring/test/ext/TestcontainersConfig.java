package bitxon.spring.test.ext;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.MountableFile;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfig {

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
    public GenericContainer wiremockContainer(DynamicPropertyRegistry registry) {
        var container = new GenericContainer("wiremock/wiremock:3.0.0-1")
            .withExposedPorts(8080)
            .withCopyFileToContainer(MountableFile.forClasspathResource("stubs"), "/home/wiremock")
            .waitingFor(Wait
                .forHttp("/__admin/mappings")
                .withMethod("GET")
                .forStatusCode(200));


        registry.add("http.exchange-client.url", () -> httpUrl(container.getHost(), container.getMappedPort(8080)));

        return container;
    }

    private static String httpUrl(String host, Integer port) {
        return String.format("http://%s:%d", host, port);
    }
}
