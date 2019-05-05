package bankaccount.config;

import bankaccount.dao.BankAccountHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoConfig {
    @Bean
    public BankAccountHandler bankAccountHandler() {
        return new BankAccountHandler();
    }
}
