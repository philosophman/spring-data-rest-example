package bankaccount.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "accountNumber"))
@Getter
@Setter
@NoArgsConstructor
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long moneyAmount;
    private Long accountNumber;
    @ManyToOne
    private Customer customer;

    public BankAccount(Long moneyAmount) {
        this.moneyAmount = moneyAmount;
    }
}
