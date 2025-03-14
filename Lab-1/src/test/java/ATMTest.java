import models.Account;
import org.junit.jupiter.api.Test;
import repository.AccountRepository;

import static org.junit.jupiter.api.Assertions.*;

public class ATMTest {
    @Test
    public void withdraw() {
        Account account = new Account(1, 1234, 50);
        account.withdraw(50);
        assertEquals(0, account.getBalance());
    }

    @Test
    public void withdrawMoreThanHave() {
        Account account = new Account(1, 1234, 50);
        assertThrows(RuntimeException.class, () -> account.withdraw(52));
    }

    @Test
    public void CreateTwoIdenticalAccounts() {
        Account account1 = new Account(1, 1234, 50);
        Account account2 = new Account(1, 1234, 50);
        AccountRepository accountRepository = new AccountRepository();
        accountRepository.createAccount(account1);
        assertThrows(RuntimeException.class, () -> accountRepository.createAccount(account2));
    }

    @Test
    public void LoginWithWrongPassword() {
        Account account1 = new Account(1, 1234, 50);
        AccountRepository accountRepository = new AccountRepository();
        accountRepository.createAccount(account1);
        assertThrows(RuntimeException.class, () -> accountRepository.Login(1, 4321));
    }

    @Test
    public void LoginWithWrongNumber() {
        Account account1 = new Account(1, 1234, 50);
        AccountRepository accountRepository = new AccountRepository();
        accountRepository.createAccount(account1);
        assertThrows(RuntimeException.class, () -> accountRepository.Login(2, 1234));
    }
}
