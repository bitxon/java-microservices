package bitxon.spring.test;

import bitxon.spring.SpringAppApplication;
import bitxon.spring.test.ext.TestcontainersConfig;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;

@ActiveProfiles("test")
@Import(TestcontainersConfig.class)
@SpringBootTest(classes = SpringAppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AbstractSpringTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private GenericContainer wiremockContainer;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        WireMock.configureFor(wiremockContainer.getHost(), wiremockContainer.getMappedPort(8080));
    }

}
