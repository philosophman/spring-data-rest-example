package bankaccount.dao;

import bankaccount.domain.BankAccount;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import java.util.Random;

@RepositoryEventHandler
public class BankAccountHandler {
    @HandleBeforeCreate
    public void beforeCreate(BankAccount account) {
        account.setAccountNumber(new Random().nextLong());
    }
}
