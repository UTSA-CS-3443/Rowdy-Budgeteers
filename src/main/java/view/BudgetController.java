// from Jordan: Ill work on this
package main.java.view;

import main.java.model.Budget;
import main.java.model.DataManager;
import main.java.model.Person;

public class BudgetController {

    private DataManager dataManager;
    private Person currentPerson;

    public BudgetController(DataManager dataManager, Person currentPerson) {
        this.dataManager = dataManager;
        this.currentPerson = currentPerson;
    }

    // UML: createBudget(budget: Budget): boolean
    public boolean createBudget(Budget budget) {
        if (budget == null) {
            return false;
        }

        return dataManager.addBudget(budget);
    }
}