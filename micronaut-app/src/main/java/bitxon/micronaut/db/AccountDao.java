package bitxon.micronaut.db;

import java.util.List;
import java.util.Optional;

import bitxon.micronaut.db.model.Account;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface AccountDao extends CrudRepository<Account, Long> {

    @Override
    List<Account> findAll();

    @Override
    Optional<Account> findById(Long id);

    @Override
    Account save(Account entity);

}
