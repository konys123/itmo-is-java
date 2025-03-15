import Exeptions.AccountAlreadyExistsException;
import Exeptions.LoginException;
import models.Account;
import org.junit.jupiter.api.Test;
import repository.AccountRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountRepositoryTest {
    @Test
    public void CreateTwoIdenticalAccounts() throws AccountAlreadyExistsException {
        Account account1 = new Account(1, "1234", 50);
        Account account2 = new Account(1, "1234", 50);
        AccountRepository accountRepository = new AccountRepository();
        accountRepository.createAccount(account1);
        assertThrows(AccountAlreadyExistsException.class, () -> accountRepository.createAccount(account2));
    }

    @Test
    public void loginWithWrongPassword() throws AccountAlreadyExistsException {
        Account account1 = new Account(1, "1234", 50);
        AccountRepository accountRepository = new AccountRepository();
        accountRepository.createAccount(account1);
        assertThrows(LoginException.class, () -> accountRepository.login(1, "4321"));
    }

    @Test
    public void loginWithWrongNumber() throws AccountAlreadyExistsException {
        Account account1 = new Account(1, "1234", 50);
        AccountRepository accountRepository = new AccountRepository();
        accountRepository.createAccount(account1);
        assertThrows(LoginException.class, () -> accountRepository.login(2, "1234"));
    }
}
