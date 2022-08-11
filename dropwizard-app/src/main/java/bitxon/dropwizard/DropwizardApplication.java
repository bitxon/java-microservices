package bitxon.dropwizard;

import bitxon.dropwizard.customization.ClasspathOrFileConfigurationSourceProvider;
import bitxon.dropwizard.db.AccountDao;
import bitxon.dropwizard.db.AccountDaoHibernateImpl;
import bitxon.dropwizard.db.model.Account;
import bitxon.dropwizard.mapper.AccountMapper;
import bitxon.dropwizard.resource.AccountResource;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.dual.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
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

        environment.jersey().register(AccountResource.class);
    }


    private final HibernateBundle<DropwizardConfiguration> hibernate = new HibernateBundle<>(Account.class) {
        @Override
        public PooledDataSourceFactory getReadSourceFactory(DropwizardConfiguration configuration) {
            return configuration.getDatabase();
        }

        @Override
        public DataSourceFactory getDataSourceFactory(DropwizardConfiguration configuration) {
            return configuration.getDatabase();
        }
    };

}
