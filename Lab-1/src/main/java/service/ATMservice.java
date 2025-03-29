package service;

import Exeptions.AccountAlreadyExistsException;
import Exeptions.InsufficientFundsException;
import Exeptions.LoginException;
import Exeptions.WrongNumberException;
import models.Account;
import models.Transaction;
import repository.AccountRepository;

import java.util.List;

public class ATMservice {
    public ATMservice(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    private final AccountRepository accountRepository;

    private Account currentAccount;

    public boolean createAccount(Integer acNum, String pin, Integer balance) {
        Account newAccount = new Account(acNum, pin, balance);
        try {
            accountRepository.createAccount(newAccount);
        } catch (AccountAlreadyExistsException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    public boolean login(Integer acNum, String pin) {
        try {
            currentAccount = accountRepository.login(acNum, pin);
        } catch (LoginException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    public Integer viewBalance() {
        return currentAccount.getBalance();
    }

    public boolean withdraw(Integer amount) {
        try {
            currentAccount.withdraw(amount);
        } catch (InsufficientFundsException | WrongNumberException ex) {
            System.out.println(ex.getMessage());
            return false;
        }

        return true;
    }

    public boolean replenishment(Integer amount) {
        try {
            currentAccount.replenishment(amount);
        } catch (WrongNumberException ex) {
            System.out.println(ex.getMessage());
            return false;
        }

        return true;
    }

    public List<Transaction> getTransactionsHistory() {
        return currentAccount.getTransactionsHistory();
    }
}
