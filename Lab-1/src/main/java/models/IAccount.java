package models;

import java.util.List;

public interface IAccount {

    Integer getBalance();

    Integer getAccountNumber();

    Integer getPin();

    List<Transaction> getTransactionsHistory();

    void replenishment(Integer sum);

    void withdraw(Integer sum);

}
