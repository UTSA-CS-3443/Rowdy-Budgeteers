package main.java.model;

public class MainBudgetController {

    private DataManager dataManager;
    private Person currentPerson;

    public MainBudgetController(DataManager dataManager, Person currentPerson) {
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