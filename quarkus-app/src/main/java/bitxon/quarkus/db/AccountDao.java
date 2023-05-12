package bitxon.quarkus.db;

import bitxon.quarkus.db.model.Account;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AccountDao implements PanacheRepository<Account> {

    public Account save(Account account) {
        this.persist(account);
        return account;
    }
}
