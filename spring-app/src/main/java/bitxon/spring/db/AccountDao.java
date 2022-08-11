package bitxon.spring.db;

import java.util.List;
import java.util.Optional;

import bitxon.spring.db.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AccountDao extends JpaRepository<Account, Long> {

    @Override
    List<Account> findAll();

    @Override
    Optional<Account> findById(Long id);

    @Override
    <S extends Account> S save(S entity);

}
