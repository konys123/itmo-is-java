package repository;

import models.IAccount;

import java.util.ArrayList;
import java.util.List;

public class AccountRepository {
    private final List<IAccount> accounts = new ArrayList<>();

    /**
     * Создает новый счет
     *
     * @param account счет, который хотим создать
     * @throws RuntimeException если такой счет уже существует
     */
    public void createAccount(IAccount account) {
        for (IAccount i : accounts) {
            if (account.getAccountNumber().equals(i.getAccountNumber()))
                throw new RuntimeException("счет с таким номером уже существует");
        }
        accounts.add(account);
    }

    private IAccount getAccount(Integer acNum) {
        for (IAccount i : accounts) {
            if (i.getAccountNumber().equals(acNum)) return i;
        }
        throw new RuntimeException("счет с таким номером не существует");
    }

    /**
     * Входит в аккаунт
     *
     * @param acNum номер счета
     * @param pin   пин-код
     * @return аккаунт
     * @throws RuntimeException если неверный номер или пин-код
     */
    public IAccount Login(Integer acNum, Integer pin) {
        IAccount foundAccount = getAccount(acNum);
        if (foundAccount.getPin().equals(pin)) return foundAccount;
        throw new RuntimeException("пароль неверный");
    }
}
