package banking;

import java.util.Scanner;

/**
 * @author Dionysios Stolis 4/20/2020
 */
public class Application {

    private static Scanner scanner;
    private static final String ONE = "1";
    private static final String ZERO = "0";
    private static final String TWO = "2";

    public static void run(String... args) {
        String databaseName = args[1];
        Bank bank = new Bank(databaseName);
        atm(bank);
    }

    public static void mainMenu() {
        scanner = new Scanner(System.in);
        System.out.println("1. Create account\n" +
                "2. Log into account\n" +
                "0. Exit");
    }

    public static void atm(Bank bank) {
        mainMenu();
        String choice = scanner.nextLine();
        switch (choice) {
            case ZERO:
                System.out.println("Bye!");
                System.exit(1);
            case ONE:
                bank.createAccount();
                System.out.println();
                atm(bank);
            case TWO:
                System.out.println("Enter your card number:");
                String cardNumber = scanner.nextLine();
                System.out.println("Enter your PIN:");
                String pin = scanner.nextLine();
                if (bank.login(cardNumber, pin)) {
                    System.out.println("You have successfully logged in!");
                    bank.accountMenu(cardNumber, pin);
                } else {
                    System.out.println("Wrong card number or PIN!");
                    atm(bank);
                }
            default:
                System.exit(1);

        }
    }
}
