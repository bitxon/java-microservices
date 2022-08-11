package bitxon.dropwizard.db;

import java.util.List;
import java.util.Optional;

import bitxon.dropwizard.db.model.Account;


public interface AccountDao {

    List<Account> findAll();

    Optional<Account> findById(Long id);

    Account save(Account account);
}
