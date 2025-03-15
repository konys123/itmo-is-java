package ui;

import models.Transaction;
import service.ATMservice;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final ATMservice atmService;
    private final Scanner scanner;

    public ConsoleUI(ATMservice atmService) {
        this.atmService = atmService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            writeStartOptions();
            int option = readInt();

            switch (option) {
                case 1 -> createAccount();
                case 2 -> login();
                case 3 -> System.exit(0);
                default -> System.out.println("Неверное значение. Попробуйте снова.");
            }
        }
    }

    private void createAccount() {
        int acNum = promptInt("Введите номер аккаунта:");
        String pin = promptString();
        if (atmService.createAccount(acNum, pin, 0)) System.out.println("Аккаунт создан");
    }

    private void login() {
        int acNum = promptInt("Введите номер аккаунта:");
        String pin = promptString();

        if (!atmService.login(acNum, pin)) return;

        while (true) {
            writeAccountOptions();
            int option = readInt();

            switch (option) {
                case 1 -> System.out.println("Ваш баланс: " + atmService.viewBalance());
                case 2 -> atmService.replenishment(promptInt("Введите сумму для пополнения:"));
                case 3 -> {
                    if (!atmService.withdraw(promptInt("Введите сумму для снятия:")))
                        System.out.println("попробуй еще раз)");
                }
                case 4 -> printTransactionHistory();
                case 5 -> {
                    System.out.println("Выход из аккаунта");
                    return;
                }
                default -> System.out.println("Неверное значение");
            }
        }
    }

    private void printTransactionHistory() {
        List<Transaction> transactions = atmService.getTransactionsHistory();
        if (transactions.isEmpty()) {
            System.out.println("История операций пуста");
        } else {
            for (Transaction transaction : transactions) {
                System.out.println(transaction);
            }
        }
    }

    private int promptInt(String message) {
        System.out.println(message);
        while (!scanner.hasNextInt()) {
            System.out.println("Введите корректное число:");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private String promptString() {
        System.out.println("Введите пин-код:");
        scanner.nextLine();
        return scanner.nextLine();
    }

    private void writeStartOptions() {
        System.out.println("Выберите опцию:");
        System.out.println("1 - Создать новый аккаунт");
        System.out.println("2 - Войти в аккаунт");
        System.out.println("3 - Завершить работу");
    }

    private void writeAccountOptions() {
        System.out.println("Выберите опцию:");
        System.out.println("1 - Посмотреть баланс");
        System.out.println("2 - Пополнить счёт");
        System.out.println("3 - Снять средства");
        System.out.println("4 - Посмотреть историю операций");
        System.out.println("5 - Назад");
    }

    private int readInt() {
        while (!scanner.hasNextInt()) {
            System.out.println("Введите корректное число:");
            scanner.next();
        }
        return scanner.nextInt();
    }
}
