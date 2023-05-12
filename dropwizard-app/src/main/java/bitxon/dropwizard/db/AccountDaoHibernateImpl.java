package bitxon.dropwizard.db;

import bitxon.dropwizard.db.model.Account;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class AccountDaoHibernateImpl extends AbstractDAO<Account> implements AccountDao {

    public AccountDaoHibernateImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(get(id));
    }

    public List<Account> findAll() {
        return list(namedTypedQuery("bitxon.dropwizard.db.model.Account.findAll"));
    }

    public Account save(Account account) {
        return persist(account);
    }

}
