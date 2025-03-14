package ui;

import models.Transaction;
import service.ATMservice;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    public ConsoleUI(ATMservice atmService) {
        this.atmService = atmService;
    }

    ATMservice atmService;

    public void Start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("выберите опцию:");
            System.out.println("1 - создать новый аккаунт");
            System.out.println("2 - войти в аккаунт");
            System.out.println("3 - завершить работу");
            int i = scanner.nextInt();
            int acNum;
            int pin;

            switch (i) {
                case 1:
                    System.out.println("введите номер аккаунта:");
                    acNum = scanner.nextInt();
                    System.out.println("введите пин-код");
                    pin = scanner.nextInt();
                    atmService.CreateAccount(acNum, pin, 0);
                    break;

                case 2:
                    System.out.println("введите номер аккаунта:");
                    acNum = scanner.nextInt();
                    System.out.println("введите пин-код");
                    pin = scanner.nextInt();
                    atmService.Login(acNum, pin);

                    while (true) {
                        System.out.println("выберите опцию:");
                        System.out.println("1 - посмотреть баланс");
                        System.out.println("2 - пополнить");
                        System.out.println("3 - снять");
                        System.out.println("4 - посмотреть историю операций");
                        System.out.println("5 - назад");
                        int j = scanner.nextInt();

                        if (j == 5) break;

                        int sum;

                        switch (j) {
                            case 1:
                                System.out.println("ваш баланс:" + atmService.ViewBalance());
                                break;

                            case 2:
                                System.out.println("введите сумму");
                                sum = scanner.nextInt();
                                atmService.replenishment(sum);
                                break;

                            case 3:
                                System.out.println("введите сумму");
                                sum = scanner.nextInt();
                                atmService.withdraw(sum);
                                break;

                            case 4:
                                List<Transaction> TrHist = atmService.getTransactionsHistory();
                                for (Transaction transaction : TrHist) {
                                    System.out.print(transaction.getTransactionType() + " ");
                                    System.out.print(transaction.getSum() + " ");
                                    System.out.print(transaction.getDate() + "\n");
                                }
                                break;

                            default:
                                break;
                        }
                    }
                    break;

                case 3:
                    return;
            }
        }
    }
}
