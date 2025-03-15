package models;

import Exeptions.InsufficientFundsException;
import lombok.Getter;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

/**
 * Реализация счета пользователя
 */
@Getter
public class Account {
    private Integer balance;

    private final Integer accountNumber;

    private final String pin;

    private final List<Transaction> transactionsHistory;

    public Account(Integer acNum, String pin, Integer balance) {
        this.accountNumber = acNum;
        this.balance = balance;
        this.transactionsHistory = new ArrayList<>();
        this.pin = BCrypt.hashpw(pin, BCrypt.gensalt(10));
    }

    /**
     * Выполняет пополнение счета
     *
     * @param amount сумма на которую нужно пополнить счет
     */
    public void replenishment(Integer amount) {
        balance += amount;
        transactionsHistory.add(new Transaction(TransactionType.REPLENISHMENT, amount));
    }

    /**
     * Выполняет снятие со счета
     *
     * @param amount сумма которую нужно снять со счета
     * @throws InsufficientFundsException если на счете недостаточно денег
     */
    public void withdraw(Integer amount) throws InsufficientFundsException {
        if (amount > balance) {
            throw new InsufficientFundsException("Недостаточно денег на счете");
        }
        balance -= amount;
        transactionsHistory.add(new Transaction(TransactionType.WITHDRAW, amount));
    }
}
