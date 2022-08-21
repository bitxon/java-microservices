package bitxon.quarkus.ext;

import java.util.Map;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class PostgresTestResourceLifecycleManager implements QuarkusTestResourceLifecycleManager {

    private PostgreSQLContainer postgreSQLContainer;


    @Override
    public Map<String, String> start() {
        postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer(DockerImageName.parse("postgres").withTag("14.4"))
            .withDatabaseName("testdb")
            .withUsername("postgres")
            .withPassword("postgres")
            .withInitScript("sql/db-test-data.sql");
        postgreSQLContainer.start();

        return Map.of(
            "quarkus.datasource.jdbc.url", postgreSQLContainer.getJdbcUrl(),
            "quarkus.datasource.username ", postgreSQLContainer.getUsername(),
            "quarkus.datasource.password", postgreSQLContainer.getPassword(),
            "quarkus.hibernate-orm.database.generation", "none"
        );
    }

    @Override
    public void stop() {
        if (postgreSQLContainer != null) {
            postgreSQLContainer.stop();
        }
    }
}
