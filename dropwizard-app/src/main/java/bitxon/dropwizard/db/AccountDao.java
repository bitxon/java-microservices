package bitxon.dropwizard.db;

import bitxon.dropwizard.db.model.Account;

import java.util.List;
import java.util.Optional;


public interface AccountDao {

    List<Account> findAll();

    Optional<Account> findById(Long id);

    Account save(Account account);
}
