package models;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


/**
 * Реализация счета пользователя
 */
@Getter
public class Account implements IAccount {
    private Integer balance;

    private final Integer accountNumber;

    private final Integer pin;

    private final List<Transaction> transactionsHistory;

    /**
     * Выполняет пополнение счета
     * @param sum сумма на которую нужно пополнить счет
     */
    @Override
    public void replenishment(Integer sum) {
        balance += sum;
        transactionsHistory.add(new Transaction(TransactionType.replenishment, sum));
    }

    /**
     * Выполняет снятие со счета
     * @param sum сумма которую нужно снять со счета
     * @throws RuntimeException если на счете недостаточно денег
     */
    @Override
    public void withdraw(Integer sum) {
        if (sum > balance) {
            throw new RuntimeException("Недостаточно денег на счете");
        }
        balance -= sum;
        transactionsHistory.add(new Transaction(TransactionType.withdraw, sum));
    }

    public Account(Integer acNum, Integer pin, Integer balance) {
        this.pin = pin;
        this.accountNumber = acNum;
        this.balance = balance;
        this.transactionsHistory = new ArrayList<>();
    }
}
