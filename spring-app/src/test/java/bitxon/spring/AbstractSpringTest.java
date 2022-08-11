package bitxon.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@ActiveProfiles("test")
@SpringBootTest(classes = SpringAppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AbstractSpringTest {

    private static final PostgreSQLContainer DB;

    static {
        DB = (PostgreSQLContainer) new PostgreSQLContainer(DockerImageName.parse("postgres").withTag("9.6.12"))
            .withDatabaseName("testdb")
            .withUsername("postgres")
            .withPassword("postgres")
            .withInitScript("sql/db-test-data.sql");
        DB.start();
    }

    @DynamicPropertySource
    static void dynamicProperty(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", DB::getJdbcUrl);
        registry.add("spring.datasource.username", DB::getUsername);
        registry.add("spring.datasource.password", DB::getPassword);
    }

    @Autowired
    private TestRestTemplate client;

    protected TestRestTemplate client() {
        return client;
    }

}
