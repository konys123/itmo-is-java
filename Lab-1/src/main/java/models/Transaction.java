package models;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
public class Transaction {
    public TransactionType transactionType;

    public Integer sum;

    public LocalDateTime date;

    Transaction(TransactionType tt, Integer sum) {
        transactionType = tt;
        this.sum = sum;
        date = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }
}
