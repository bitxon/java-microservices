package bitxon.quarkus.test.ext;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.MountableFile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

public class WiremockTestResourceLifecycleManager implements QuarkusTestResourceLifecycleManager {

    private GenericContainer wiremockContainer;


    @Override
    public void init(Map<String, String> initArgs) {
        wiremockContainer = new GenericContainer("wiremock/wiremock:3.0.0-1")
            .withExposedPorts(8080)
            .withCopyFileToContainer(MountableFile.forClasspathResource("stubs"), "/home/wiremock")
            .waitingFor(Wait
                .forHttp("/__admin/mappings")
                .withMethod("GET")
                .forStatusCode(200));
    }

    @Override
    public Map<String, String> start() {
        wiremockContainer.start();

        return Map.of(
            "quarkus.rest-client.exchange-client.url", String.format("http://%s:%d", wiremockContainer.getHost(), wiremockContainer.getMappedPort(8080))
        );
    }

    @Override
    public void stop() {
        if (wiremockContainer != null) {
            wiremockContainer.stop();
        }
    }

    @Override
    public void inject(TestInjector testInjector) {
        testInjector.injectIntoFields(
            wiremockContainer,
            field -> field.isAnnotationPresent(Inject.class)
        );
    }


    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Inject {
    }
}
