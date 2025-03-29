package models;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Transaction {
    private final TransactionType transactionType;

    private final Integer amount;

    private final LocalDateTime date;

    Transaction(TransactionType tt, Integer amount) {
        transactionType = tt;
        this.amount = amount;
        date = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("%s %d %s", transactionType, amount, date);
    }
}
