package main.java.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import main.java.BankingApplication;
import main.java.model.Budget;
import main.java.model.DataManager;
import main.java.model.Person;
import main.java.model.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class BudgetController {

    private DataManager dataManager;
    private Person currentPerson;

    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextField budgetAmountField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private Label messageLabel;

    @FXML private TableView<Budget> budgetsTable;
    @FXML private TableColumn<Budget, String> budgetIDColumn;
    @FXML private TableColumn<Budget, String> categoryColumn;
    @FXML private TableColumn<Budget, String> budgetLimitColumn;
    @FXML private TableColumn<Budget, String> amountSpentColumn;
    @FXML private TableColumn<Budget, String> remainingColumn;
    @FXML private TableColumn<Budget, String> statusColumn;

    public BudgetController(DataManager dataManager, Person currentPerson) {
        this.dataManager = dataManager;
        this.currentPerson = currentPerson;
    }

    @FXML
    public void initialize() {
        // Set up the available budget categories.
        categoryComboBox.setItems(FXCollections.observableArrayList(
                "Food", "Transportation", "Shopping",
                "Entertainment", "Housing", "Utilities",
                "Healthcare", "Other"
        ));

        setupTable();
        refreshBudgets();
    }

    // Connects each table column to the corresponding budget information.
    private void setupTable() {

        budgetsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        budgetIDColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(
                        cellData.getValue().getBudgetID()));

        categoryColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(
                        cellData.getValue().getBudgetType()));

        budgetLimitColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(String.format(
                        Locale.US, "$%,.2f",
                        cellData.getValue().getBudgetLimit())));

        amountSpentColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(String.format(
                        Locale.US, "$%,.2f",
                        calculateAmountSpent(cellData.getValue()))));

        remainingColumn.setCellValueFactory(cellData -> {
            Budget budget = cellData.getValue();

            double remaining = budget.getBudgetLimit()
                    - calculateAmountSpent(budget);

            return new ReadOnlyStringWrapper(String.format(
                    Locale.US, "$%,.2f", remaining));
        });

        statusColumn.setCellValueFactory(cellData -> {
            Budget budget = cellData.getValue();

            double spent = calculateAmountSpent(budget);

            String status = spent > budget.getBudgetLimit()
                    ? "Over Budget" : "On Track";

            return new ReadOnlyStringWrapper(status);
        });
    }

    // Calculates spending that matches the budget category and date range.
    private double calculateAmountSpent(Budget budget) {
        double totalSpent = 0;

        for (Transaction transaction : dataManager.getTransactions()) {
            if (!transaction.getPurchaseType().equalsIgnoreCase(
                    budget.getBudgetType())) {
                continue;
            }

            try {
                LocalDate transactionDate =
                        LocalDate.parse(transaction.getDate());

                if (!transactionDate.isBefore(budget.getStartDate())
                        && !transactionDate.isAfter(budget.getEndDate())) {

                    totalSpent += transaction.getCost();
                }

            } catch (Exception e) {
                // Skip transactions with dates that cannot be read.
            }
        }

        return totalSpent;
    }

    // Reloads the table using the current budget list.
    private void refreshBudgets() {
        List<Budget> budgetList = new ArrayList<>(dataManager.getBudgets());

        budgetList.sort(Comparator.comparing(Budget::getBudgetType));

        budgetsTable.setItems(FXCollections.observableArrayList(budgetList));
    }

    @FXML
    private void handleAddBudget() {
        String category = categoryComboBox.getValue();
        String amountText = budgetAmountField.getText().trim();

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (category == null || amountText.isEmpty()
                || startDate == null || endDate == null) {
            messageLabel.setText("Please complete all budget fields.");
            return;
        }

        double amount;

        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            messageLabel.setText("Please enter a valid budget amount.");
            return;
        }

        if (amount <= 0 || !Double.isFinite(amount)) {
            messageLabel.setText(
                    "The budget amount must be greater than zero.");
            return;
        }

        if (endDate.isBefore(startDate)) {
            messageLabel.setText(
                    "The end date cannot be before the start date.");
            return;
        }

        String budgetID = UUID.randomUUID().toString();

        Budget budget = new Budget(
                budgetID, category, amount, startDate, endDate);

        if (dataManager.addBudget(budget)) {
            messageLabel.setText("Budget added successfully!");

            clearFields();
            refreshBudgets();
        } else {
            messageLabel.setText("Unable to add budget.");
        }
    }

    @FXML
    private void handleUpdateBudget() {
        Budget selectedBudget =
                budgetsTable.getSelectionModel().getSelectedItem();

        if (selectedBudget == null) {
            messageLabel.setText("Please select a budget to update.");
            return;
        }

        String amountText = budgetAmountField.getText().trim();

        if (amountText.isEmpty()) {
            messageLabel.setText(
                    "Enter the new limit in the Budget Amount field.");
            return;
        }

        double newAmount;

        try {
            newAmount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            messageLabel.setText("Please enter a valid budget amount.");
            return;
        }

        if (newAmount <= 0 || !Double.isFinite(newAmount)) {
            messageLabel.setText(
                    "The budget amount must be greater than zero.");
            return;
        }

        selectedBudget.setBudgetLimit(newAmount);
        dataManager.updateBudget(selectedBudget);

        messageLabel.setText("Budget updated successfully!");

        budgetAmountField.clear();
        refreshBudgets();
    }

    @FXML
    private void handleDeleteBudget() {
        Budget selectedBudget =
                budgetsTable.getSelectionModel().getSelectedItem();
        if (selectedBudget == null) {
            messageLabel.setText("Please select a budget to delete.");
            return;
        }
        if (dataManager.deleteBudget(selectedBudget.getBudgetID())) {
            messageLabel.setText("Budget deleted successfully!");
            refreshBudgets();
        } else {
            messageLabel.setText("Unable to delete budget.");
        }
    }


    @FXML
    private void handleRefresh() {
        refreshBudgets();
        messageLabel.setText("Budgets refreshed.");
    }

    private void clearFields() {
        categoryComboBox.getSelectionModel().clearSelection();
        budgetAmountField.clear();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
    }

    // Navigation methods

    @FXML
    private void showDashboard() {
        changeScreen("main-screen.fxml");
    }

    @FXML
    private void showTransactions() {
        changeScreen("transaction-screen.fxml");
    }

    @FXML
    private void showReports() {
        changeScreen("report-screen.fxml");
    }

    @FXML
    private void handleLogout() {
        try {
            BankingApplication.changeScene(
                    "login-screen.fxml", dataManager, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeScreen(String fxmlFile) {
        try {
            BankingApplication.changeScene(
                    fxmlFile, dataManager, currentPerson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}