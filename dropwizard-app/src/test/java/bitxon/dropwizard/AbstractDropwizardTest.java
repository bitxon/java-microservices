package bitxon.dropwizard;


import static io.dropwizard.testing.ConfigOverride.config;

import javax.ws.rs.client.Client;


import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.DropwizardTestSupport;
import io.dropwizard.testing.ResourceHelpers;


public abstract class AbstractDropwizardTest {

    private static final DropwizardTestSupport<DropwizardConfiguration> APP;
    private static final Client CLIENT;

    static {
        APP = new DropwizardTestSupport<>(DropwizardApplication.class,
            ResourceHelpers.resourceFilePath("config.yml"),
            config("server.applicationConnectors[0].port", "0")
        );
        try {
            // This trick helps to spin up application once
            APP.before();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        CLIENT = new JerseyClientBuilder(APP.getEnvironment()).build("test-client");

    }

    protected Client client() {
        return CLIENT;
    }

    protected int appLocalPort() {
        return APP.getLocalPort();
    }

}
