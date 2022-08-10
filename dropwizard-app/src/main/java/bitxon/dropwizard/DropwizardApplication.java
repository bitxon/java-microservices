package bitxon.dropwizard;

import bitxon.dropwizard.customization.ClasspathOrFileConfigurationSourceProvider;
import bitxon.dropwizard.resource.AccountResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class DropwizardApplication extends Application<DropwizardConfiguration> {

    public static void main(final String[] args) throws Exception {
        new DropwizardApplication().run(args);
    }

    @Override
    public String getName() {
        return "dropwizard-app";
    }

    @Override
    public void initialize(final Bootstrap<DropwizardConfiguration> bootstrap) {
        // Allow to read config.yml from classpath
        bootstrap.setConfigurationSourceProvider(new ClasspathOrFileConfigurationSourceProvider());
    }

    @Override
    public void run(final DropwizardConfiguration configuration, final Environment environment) {
        environment.jersey().register(AccountResource.class);
    }

}
