package bitxon.micronaut;

import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
class AbstractMicronautTest {

    @Inject
    private EmbeddedApplication<?> application;
    @Inject
    @Client("/accounts")
    private HttpClient client;

    protected HttpClient client() {
        return client;
    }

}
