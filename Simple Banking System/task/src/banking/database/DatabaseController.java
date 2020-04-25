package banking.database;

/**
 * @author Dionysios Stolis
 */
public class DatabaseController {

    private DatabaseService databaseService = new DatabaseService();
    private String databaseName;

    public void init(String databaseName) {
        this.databaseName = databaseName;
        databaseService.createNewDatabase(databaseName);
        databaseService.createNewTable(databaseName);
    }

    public int getBalance(String cardNumber) {
        return databaseService.selectBalance(databaseName, cardNumber);
    }

    public void transfer(String cardNumber, int amount) {
        databaseService.update(databaseName, cardNumber, amount);
    }

    public void close(String cardNumber) {
        databaseService.delete(databaseName, cardNumber);
    }

    public void addAccount(String creditCard, String pin) {
        databaseService.insert(databaseName, creditCard, pin);
    }

    public boolean isValid(String creditCard) {
        return databaseService.selectBalance(databaseName, creditCard) != -1;
    }

    public boolean login(String creditCard, String pin) {
        return databaseService.selectAccount(databaseName,creditCard,pin) != -1;
    }
}
