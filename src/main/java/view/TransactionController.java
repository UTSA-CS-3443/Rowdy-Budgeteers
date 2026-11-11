// from Jordan: Ill work on this
package main.java.view;

import main.java.model.DataManager;
import main.java.model.Person;
import main.java.model.Transaction;

public class TransactionController {

    private DataManager dataManager;
    private Person currentPerson;

    public TransactionController(DataManager dataManager, Person currentPerson) {
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
