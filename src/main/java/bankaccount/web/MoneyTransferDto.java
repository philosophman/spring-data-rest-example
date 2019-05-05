package bankaccount.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoneyTransferDto {
    private long srcAccountNumber; // money source
    private long dstAccountNumber; // money destination
    private long moneyAmount;
}
