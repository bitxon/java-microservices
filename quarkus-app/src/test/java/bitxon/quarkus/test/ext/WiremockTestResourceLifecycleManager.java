package bitxon.quarkus.test.ext;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.util.Map;

public class WiremockTestResourceLifecycleManager implements QuarkusTestResourceLifecycleManager {

    private GenericContainer wiremock;

    @Override
    public Map<String, String> start() {
        wiremock = new GenericContainer("wiremock/wiremock:2.35.0")
            .withExposedPorts(8080)
            .withClasspathResourceMapping("stubs", "/home/wiremock", BindMode.READ_ONLY)
            .waitingFor(Wait
                .forHttp("/__admin/mappings")
                .withMethod("GET")
                .forStatusCode(200));
        wiremock.start();
        WireMock.configureFor(wiremock.getMappedPort(8080));

        return Map.of(
            "quarkus.rest-client.exchange-client.url", String.format("http://%s:%d", wiremock.getHost(), wiremock.getMappedPort(8080))
        );
    }

    @Override
    public void stop() {
        if (wiremock != null) {
            wiremock.stop();
        }
    }
}
