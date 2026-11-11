package main.java.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Locale;

public class SpendingReport {

    // Attributes from UML
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalSpending;

    // Constructor
    public SpendingReport(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalSpending = 0.0;
    }

    // Calculate total spending within the selected date range
    public double calculateTotal(List<Transaction> transactions) {

        totalSpending = 0.0;
        if (transactions == null) {
            return totalSpending;
        }

        for (Transaction transaction : transactions) {
            LocalDate transactionDate = transaction.getDate();
            if (transactionDate != null
                    && !transactionDate.isBefore(startDate)
                    && !transactionDate.isAfter(endDate)) {

                totalSpending += transaction.getCost();
            }
        }
        return totalSpending;
    }
    // Group spending by transaction category
    public Map<String, Double> spendingByCategory(
            List<Transaction> transactions) {

        Map<String, Double> spending = new HashMap<>();

        if (transactions == null) {
            return spending;
        }

        for (Transaction transaction : transactions) {

            LocalDate transactionDate = transaction.getDate();

            if (transactionDate != null
                    && !transactionDate.isBefore(startDate)
                    && !transactionDate.isAfter(endDate)) {

                String category = transaction.getPurchaseType();
                double cost = transaction.getCost();
                spending.put(category, spending.getOrDefault(category, 0.0) + cost);
            }
        }
        return spending;
    }

    // Generate a report for a specific budget
    public String generateMonthlyBudgetReport(
            Budget budget, List<Transaction> transactions) {
        if (budget == null) {
            return "Unable to generate report: Budget not found.";
        }
        double budgetSpending = 0.0;

        if (transactions != null) {

            for (Transaction transaction : transactions) {
                LocalDate transactionDate = transaction.getDate();
                if (transactionDate != null
                        && !transactionDate.isBefore(startDate)
                        && !transactionDate.isAfter(endDate)
                        && transaction.getPurchaseType()
                        .equalsIgnoreCase(budget.getBudgetType())) {
                    budgetSpending += transaction.getCost();
                }
            }
        }

        double remaining = budget.getBudgetLimit() - budgetSpending;

        String status;

        if (remaining < 0) {
            status = "Over Budget";
        } else if (remaining <= budget.getBudgetLimit() * 0.10) {
            status = "Near Budget Limit";
        } else {
            status = "On Track";
        }

        return String.format(Locale.US,
                "Budget Report: %s%n"
                        + "Date Range: %s to %s%n"
                        + "Budget Limit: $%.2f%n"
                        + "Total Spending: $%.2f%n"
                        + "Remaining: $%.2f%n"
                        + "Status: %s",
                budget.getBudgetType(),
                startDate,
                endDate,
                budget.getBudgetLimit(),
                budgetSpending,
                remaining,
                status);
    }

    // Return total spending
    public double getTotalSpending() {
        return totalSpending;
    }
}

//monthly summary and category calculations