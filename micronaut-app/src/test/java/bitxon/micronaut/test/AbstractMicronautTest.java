package bitxon.micronaut.test;

import java.util.Map;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@MicronautTest // default environments is 'test'
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class AbstractMicronautTest implements TestPropertyProvider {

    private static final WireMockServer WIREMOCK;

    static {
        WIREMOCK = new WireMockServer(WireMockConfiguration.options()
            .usingFilesUnderClasspath("stubs") // Loading stubs from common-wiremock
            .dynamicPort()
        );
        WIREMOCK.start();
        WireMock.configureFor(WIREMOCK.port());
    }

    @Inject
    private EmbeddedApplication<?> application;

    @Override
    public Map<String, String> getProperties() {
        return Map.of(
            "micronaut.http.services.exchange-client.url", WIREMOCK.baseUrl()
        );
    }
}
