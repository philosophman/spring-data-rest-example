package bankaccount.web;

import bankaccount.service.BankService;
import bankaccount.utils.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    private BankService service;

    @Autowired
    public Controller(BankService service) {
        Assert.notNull("service", service);
        this.service = service;
    }

    @PostMapping("/transfers")
    public HttpEntity<?> transferMoney(@RequestBody final MoneyTransferDto transfer) {
        service.transferMoney(
                transfer.getSrcAccountNumber(),
                transfer.getDstAccountNumber(),
                transfer.getMoneyAmount());
        return HttpEntity.EMPTY;
    }

}
