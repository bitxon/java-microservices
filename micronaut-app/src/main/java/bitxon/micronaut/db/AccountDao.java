package bitxon.micronaut.db;

import bitxon.micronaut.db.model.Account;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountDao extends JpaRepository<Account, Long> {

    @Override
    List<Account> findAll();

    @Override
    Optional<Account> findById(Long id);

    @Override
    <S extends Account> S save(S entity);

}
