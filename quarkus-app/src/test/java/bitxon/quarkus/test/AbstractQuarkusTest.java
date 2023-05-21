package bitxon.quarkus.test;

import bitxon.quarkus.test.ext.PostgresTestResourceLifecycleManager;
import bitxon.quarkus.test.ext.WiremockTestResourceLifecycleManager;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.quarkus.test.common.QuarkusTestResource;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.GenericContainer;

@QuarkusTestResource(PostgresTestResourceLifecycleManager.class)
@QuarkusTestResource(WiremockTestResourceLifecycleManager.class)
public abstract class AbstractQuarkusTest {


    // Will be injected by {WiremockTestResourceLifecycleManager#inject()}
    @WiremockTestResourceLifecycleManager.Inject
    GenericContainer wiremockContainer;

    @BeforeEach
    public void beforeEach() {
        WireMock.configureFor(wiremockContainer.getMappedPort(8080));
    }

}
