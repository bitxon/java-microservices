package bitxon.quarkus.test.ext;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

public class PostgresTestResourceLifecycleManager implements QuarkusTestResourceLifecycleManager {

    private PostgreSQLContainer postgreSQLContainer;


    @Override
    public void init(Map<String, String> initArgs) {
        postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer("postgres:18.1")
            .withDatabaseName("testdb")
            .withUsername("postgres")
            .withPassword("postgres")
            .withInitScript("sql/db-test-data.sql");
    }

    @Override
    public Map<String, String> start() {
        postgreSQLContainer.start();

        return Map.of(
            "quarkus.datasource.jdbc.url", postgreSQLContainer.getJdbcUrl(),
            "quarkus.datasource.username", postgreSQLContainer.getUsername(),
            "quarkus.datasource.password", postgreSQLContainer.getPassword(),
            "quarkus.hibernate-orm.schema-management.strategy", "none"
        );
    }

    @Override
    public void stop() {
        if (postgreSQLContainer != null) {
            postgreSQLContainer.stop();
        }
    }
}
