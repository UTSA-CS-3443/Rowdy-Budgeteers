package main.java.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Budget implements Serializable {
// serializable allows java to convert objects into a format that can be saved and recontrsucted
    // without it, serialization used by DataManager would not work
    private String budgetID;
    private String budgetType;
    private double budgetLimit;
    private LocalDate startDate;
    private LocalDate endDate;

    public Budget(String budgetID, String budgetType,
                  double budgetLimit, LocalDate startDate,
                  LocalDate endDate) {

        this.budgetID = budgetID;
        this.budgetType = budgetType;
        this.budgetLimit = budgetLimit;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Validates the information before the budget is created.
    public boolean validateBudget() {
        return budgetID != null && !budgetID.trim().isEmpty()
                && budgetType != null && !budgetType.trim().isEmpty()
                && budgetLimit > 0
                && startDate != null
                && endDate != null
                && !endDate.isBefore(startDate);
    }

    public String getBudgetID() {
        return budgetID;
    }

    public String getBudgetType() {
        return budgetType;
    }

    public double getBudgetLimit() {
        return budgetLimit;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setBudgetLimit(double budgetLimit) {
        this.budgetLimit = budgetLimit;
    }
}
