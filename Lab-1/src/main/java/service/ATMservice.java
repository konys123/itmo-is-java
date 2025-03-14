package service;

import models.Account;
import models.IAccount;
import models.Transaction;
import repository.AccountRepository;

import java.util.List;

public class ATMservice {
    public ATMservice(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }
    AccountRepository accountRepository;

    IAccount currentAccount;

    public void CreateAccount(Integer acNum, Integer pin, Integer balance) {
        IAccount newAccount = new Account(acNum, pin, balance);
        try {
            accountRepository.createAccount(newAccount);
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }

    public void Login(Integer acNum, Integer pin) {
        try {
            currentAccount = accountRepository.Login(acNum, pin);
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }

    public Integer ViewBalance(){
        return currentAccount.getBalance();
    }

    public void withdraw(Integer sum){
        try {
            currentAccount.withdraw(sum);
        }
        catch (RuntimeException ex){
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }

    public void replenishment(Integer sun){
        try {
            currentAccount.replenishment(sun);
        }catch (RuntimeException ex){
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }

    public List<Transaction> getTransactionsHistory(){
        return currentAccount.getTransactionsHistory();
    }
}
