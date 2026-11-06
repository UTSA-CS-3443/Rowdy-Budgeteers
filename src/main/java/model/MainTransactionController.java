package main.java.model;

public class MainTransactionController {

    private DataManager dataManager;
    private Person currentPerson;

    public MainTransactionController(DataManager dataManager, Person currentPerson) {
        this.dataManager = dataManager;
        this.currentPerson = currentPerson;
    }

    // UML: createTransaction(transaction: Transaction): boolean
    public boolean createTransaction(Transaction transaction) {
        if (transaction == null) {
            return false;
        }

        return dataManager.addTransaction(transaction);
    }
}
