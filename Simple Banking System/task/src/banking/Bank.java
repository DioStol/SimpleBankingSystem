package banking;

import banking.database.DatabaseController;

import java.util.Random;
import java.util.Scanner;

/**
 * @author Dionysios Stolis 4/15/2020
 */
public class Bank {

    private DatabaseController databaseController = new DatabaseController();
    private static final String BANK_NUMBER = "4000000";
    private static Scanner scanner = new Scanner(System.in);

    public Bank(String databaseName) {
        databaseController.init(databaseName);
    }

    public void createAccount() {
        try {
            System.out.println();
            System.out.println("Your card have been created");
            String cardNumber = generateCreditNumber();
            String pin = generatePin();
            System.out.println("Your card number:\n" + cardNumber);
            System.out.println("Your card PIN:\n" + pin);
            databaseController.addAccount(cardNumber, pin);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean login(String cardNumber, String pin) {
        return databaseController.login(cardNumber, pin);
    }

    private String generateCreditNumber() {
        StringBuilder cardNumber = new StringBuilder(BANK_NUMBER);
        Random random = new Random();
        int number;
        for (int i = 0; i < 8; i++) {
            number = random.nextInt(9);
            cardNumber.append(number);
        }
        int checkSum = getCheckDigit(cardNumber.toString());
        cardNumber.append(checkSum);
        return cardNumber.toString();
    }

    private String generatePin() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            builder.append(random.nextInt(9));
        }
        return builder.toString();
    }

    private int getCheckDigit(String number) {
        int sum = 0;
        for (int i = 0; i < number.length(); i++) {
            int digit = Integer.parseInt(number.substring(i, (i + 1)));

            if ((i % 2) == 0) {
                digit = digit * 2;
                if (digit > 9) {
                    digit = (digit / 10) + (digit % 10);
                }
            }
            sum += digit;
        }
        int mod = sum % 10;
        return ((mod == 0) ? 0 : 10 - mod);
    }

    private boolean isValid(String creditNumber) {
        if (creditNumber.isEmpty())
            return false;
        int nDigits = creditNumber.length();

        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--) {

            int d = creditNumber.charAt(i) - '0';

            if (isSecond == true)
                d = d * 2;
            nSum += d / 10;
            nSum += d % 10;

            isSecond = !isSecond;
        }
        return (nSum % 10 == 0);
    }

    void accountMenu(String cardNumber, String pin) {
        String choice = scanner.nextLine();
        switch (choice) {
            case "0":
                System.out.println("Bye!");
                System.exit(1);
            case "1":
                System.out.println(databaseController.getBalance(cardNumber));
                accountMenu(cardNumber, pin);
            case "2":
                System.out.println("Enter the amount");
                int newIncome = Integer.parseInt(scanner.nextLine());
                databaseController.transfer(cardNumber, newIncome);
                accountMenu(cardNumber, pin);
            case "3":
                System.out.println("Enter recipient card number!");
                String recipientCardNumber = scanner.nextLine();
                if (!recipientCardNumber.equals(cardNumber)) {
                    if (isValid(recipientCardNumber)) {
                        if (databaseController.isValid(recipientCardNumber)){
                            System.out.println("Enter money amount:");
                            int amount = Integer.parseInt(scanner.nextLine());
                            databaseController.transfer(recipientCardNumber,amount);
                            System.out.println("Transfer has finished successfully!");
                        }
                    } else {
                        System.out.println("Probably you made mistake in card number. Please try again!");
                    }
                } else {
                    System.out.println("You can't transfer money to the same account!");
                }
                accountMenu(cardNumber, pin);
            case "4":
                databaseController.close(cardNumber);
                accountMenu(cardNumber, pin);
            default:
                accountMenu(cardNumber, pin);

        }
    }
}
