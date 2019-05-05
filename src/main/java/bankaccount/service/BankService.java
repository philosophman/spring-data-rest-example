package bankaccount.service;

public interface BankService {
    /**
     * Transfers money from source to destination.
     * @param srcAccountNumber money source.
     * @param dstAccountNumber money destination.
     */
    void transferMoney(long srcAccountNumber, long dstAccountNumber, long moneyAmount);
}
