package bitxon.dropwizard;

import bitxon.dropwizard.client.exchange.ExchangeClient;
import bitxon.dropwizard.customization.ClasspathOrFileConfigurationSourceProvider;
import bitxon.dropwizard.db.AccountDao;
import bitxon.dropwizard.db.AccountDaoHibernateImpl;
import bitxon.dropwizard.db.model.Account;
import bitxon.dropwizard.errorhandler.DirtyTrickExceptionHandler;
import bitxon.dropwizard.errorhandler.JerseyViolationExceptionHandler;
import bitxon.dropwizard.errorhandler.ResourceNotFoundExceptionHandler;
import bitxon.dropwizard.mapper.AccountMapper;
import bitxon.dropwizard.resource.AccountResource;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.mapstruct.factory.Mappers;

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

        bootstrap.addBundle(hibernate);

        // TODO remove when Dropwizard 5 will release a fix with proper registration of Jackson's polymorphic subtypes
        bootstrap.getObjectMapper().registerSubtypes(
            io.dropwizard.jetty.HttpConnectorFactory.class,
            io.dropwizard.jetty.HttpsConnectorFactory.class
        );
    }

    @Override
    public void run(final DropwizardConfiguration configuration, final Environment environment) {


        var sessionFactory = hibernate.getSessionFactory();
        var accountDao = new AccountDaoHibernateImpl(sessionFactory);
        var accountMapper = Mappers.getMapper(AccountMapper.class);

        environment.jersey().register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(accountDao).to(AccountDao.class);
                bind(accountMapper).to(AccountMapper.class);
            }
        });


        var client = new JerseyClientBuilder(environment)
            .using(configuration.getExchangeClientConfig())
            .build("exchangeClient");
        var exchangeClient = new ExchangeClient(client, configuration.getExchangeClientConfig());
        environment.jersey().register(exchangeClient);

        var objectMapper = environment.getObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY); // not really necessary, but shows how to customize Jackson


        environment.jersey().register(AccountResource.class);

        environment.jersey().register(DirtyTrickExceptionHandler.class);
        environment.jersey().register(JerseyViolationExceptionHandler.class);
        environment.jersey().register(ResourceNotFoundExceptionHandler.class);
    }


    private final HibernateBundle<DropwizardConfiguration> hibernate = new HibernateBundle<>(Account.class) {

        @Override
        public DataSourceFactory getDataSourceFactory(DropwizardConfiguration configuration) {
            return configuration.getDatabase();
        }
    };

}
