import Exeptions.InsufficientFundsException;
import Exeptions.WrongNumberException;
import models.Account;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountTest {
    @Test
    public void withdraw() throws InsufficientFundsException, WrongNumberException {
        Account account = new Account(1, "1234", 50);
        account.withdraw(50);
        assertEquals(0, account.getBalance());
    }

    @Test
    public void withdrawMoreThanHave() {
        Account account = new Account(1, "1234", 50);
        assertThrows(InsufficientFundsException.class, () -> account.withdraw(52));
    }

    @Test
    public void replenishment() throws WrongNumberException {
        Account account = new Account(1, "1234", 0);
        account.replenishment(100);
        assertEquals(100, account.getBalance());
    }

    @Test
    public void negativeReplenishment() {
        Account account = new Account(1, "1234", 0);
        assertThrows(WrongNumberException.class, () -> account.replenishment(-100));
    }

    @Test
    public void negativeWithdraw() {
        Account account = new Account(1, "1234", 0);
        assertThrows(WrongNumberException.class, () -> account.withdraw(-100));
    }
}
