package bankaccount.service;

import bankaccount.dao.BankAccountDao;
import bankaccount.dao.CustomerDao;
import bankaccount.utils.Assert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BankServiceImpl implements BankService {
    private final CustomerDao customerDao;
    private final BankAccountDao accountDao;

    public BankServiceImpl(CustomerDao customerDao, BankAccountDao accountRepository) {
        Assert.notNull("customerDao", customerDao);
        Assert.notNull("accountRepository", accountRepository);
        this.customerDao = customerDao;
        this.accountDao = accountRepository;
    }

    @Transactional
    @Override
    public void transferMoney(long srcAccountNumber, long dstAccountNumber, long moneyAmount) {
        if(!accountDao.findByAccountNumber(srcAccountNumber).isPresent()) {
            throw new ServiceException(
                    ServiceExceptionCode.SRC_ACCOUNT_NOT_EXISTS,
                    "source bank account does not exists");
        }
        if(!accountDao.findByAccountNumber(dstAccountNumber).isPresent()) {
            throw new ServiceException(
                    ServiceExceptionCode.DST_ACCOUNT_NOT_EXISTS,
                    "destination bank account does not exists");
        }
        accountDao.changeMoneyAmount(srcAccountNumber, -moneyAmount);
        accountDao.changeMoneyAmount(dstAccountNumber, moneyAmount);
    }
}
