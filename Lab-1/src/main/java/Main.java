import repository.AccountRepository;
import service.ATMservice;
import ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        AccountRepository accountRepository = new AccountRepository();
        ATMservice atmService = new ATMservice(accountRepository);
        ConsoleUI ui = new ConsoleUI(atmService);
        ui.start();
    }
}