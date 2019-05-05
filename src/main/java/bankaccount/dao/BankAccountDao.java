package bankaccount.dao;

import bankaccount.domain.BankAccount;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

@RepositoryRestResource
public interface BankAccountDao extends PagingAndSortingRepository<BankAccount, Long> {
    /**
     * Increases or decreases money amount by the factor 'delta'.
     */
    @RestResource(exported = false)
    @Modifying
    @Query("update #{#entityName} set moneyAmount = moneyAmount + :delta where accountNumber = :accountNumber")
    void changeMoneyAmount(long accountNumber, long delta);

    Optional<BankAccount> findByAccountNumber(long accountNumber);
}
