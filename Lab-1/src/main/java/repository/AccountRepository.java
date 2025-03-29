package repository;

import Exeptions.AccountAlreadyExistsException;
import Exeptions.LoginException;
import models.Account;
import org.mindrot.jbcrypt.BCrypt;


import java.util.HashMap;
import java.util.Map;

public class AccountRepository {
    private final Map<Integer, Account> accounts = new HashMap<>();

    /**
     * Создает новый счет
     *
     * @param account счет, который хотим создать
     * @throws AccountAlreadyExistsException если такой счет уже существует
     */
    public void createAccount(Account account) throws AccountAlreadyExistsException {
        if (getAccount(account.getAccountNumber()) != null)
            throw new AccountAlreadyExistsException("счет с таким номером уже существует");
        accounts.put(account.getAccountNumber(), account);
    }

    private Account getAccount(Integer acNum) {
        if (accounts.containsKey(acNum)) return accounts.get(acNum);
        return null;
    }

    /**
     * Входит в аккаунт
     *
     * @param acNum номер счета
     * @param pin   пин-код
     * @return аккаунт
     * @throws LoginException если неверный номер или пин-код
     */
    public Account login(Integer acNum, String pin) throws LoginException {
        Account foundAccount = getAccount(acNum);
        if (foundAccount != null && BCrypt.checkpw(pin, foundAccount.getPin())) return foundAccount;
        throw new LoginException("неверный номер или пароль");
    }
}
