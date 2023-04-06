package bitxon.quarkus.db;

import jakarta.enterprise.context.ApplicationScoped;

import bitxon.quarkus.db.model.Account;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class AccountDao implements PanacheRepository<Account> {

    public Account save(Account account) {
        this.persist(account);
        return account;
    }
}
