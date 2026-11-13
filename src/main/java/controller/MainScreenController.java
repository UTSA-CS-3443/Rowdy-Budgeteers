package main.java.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.ReadOnlyStringWrapper;

import main.java.BankingApplication;
import main.java.model.DataManager;
import main.java.model.Person;
import main.java.model.Transaction;
import main.java.model.Budget;
import main.java.model.SpendingReport;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


public class MainScreenController {

    // Shared application data
    private DataManager dataManager;
    private Person currentPerson;


    // FXML COMPONENTS

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label totalBalanceLabel;

    @FXML
    private Label monthlySpendingLabel;

    @FXML
    private Label activeBudgetsLabel;

    @FXML
    private TableView<Transaction> transactionsTable;

    @FXML
    private TableColumn<Transaction, String> dateColumn;

    @FXML
    private TableColumn<Transaction, String> categoryColumn;

    @FXML
    private TableColumn<Transaction, String> amountColumn;

    @FXML
    private TableColumn<Transaction, String> descriptionColumn;


    // CONSTRUCTOR

    public MainScreenController(DataManager dataManager,
                                Person currentPerson) {

        this.dataManager = dataManager;
        this.currentPerson = currentPerson;
    }


    // INITIALIZE DASHBOARD

    @FXML
    public void initialize() {

        // Display the logged-in user's name
        welcomeLabel.setText(
                "Welcome Back, " + currentPerson.getName() + "!"
        );

        // Update the financial summary cards
        updateFinancialSummary();

        // Populate the recent transactions table
        loadRecentTransactions();
    }


    // FINANCIAL SUMMARY

    private void updateFinancialSummary() {

        // Account balance is not currently available in the model
        totalBalanceLabel.setText("N/A");

        // Calculate spending for the current month
        LocalDate today = LocalDate.now();

        LocalDate startOfMonth = today.withDayOfMonth(1);

        SpendingReport report = new SpendingReport(
                startOfMonth, today
        );

        double monthlySpending = report.calculateTotal(
                dataManager.getTransactions()
        );

        monthlySpendingLabel.setText(
                String.format(Locale.US, "$%,.2f", monthlySpending)
        );

        // Count budgets that are currently active
        int activeBudgets = 0;

        for (Budget budget : dataManager.getBudgets()) {

            if (!today.isBefore(budget.getStartDate())
                    && !today.isAfter(budget.getEndDate())) {

                activeBudgets++;
            }
        }

        activeBudgetsLabel.setText(
                String.valueOf(activeBudgets)
        );
    }


    // RECENT TRANSACTIONS TABLE

    private void loadRecentTransactions() {

        // Connect transaction information to the table columns
        dateColumn.setCellValueFactory(cellData -> {
            LocalDate date = cellData.getValue().getDate();
            return new ReadOnlyStringWrapper(date != null ? date.toString() : "");
        });


        categoryColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(
                        cellData.getValue().getPurchaseType()
                )
        );

        amountColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(
                        String.format(
                                Locale.US,
                                "$%,.2f",
                                cellData.getValue().getCost()
                        )
                )
        );

        descriptionColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(
                        cellData.getValue().getPurchaseName()
                )
        );

        // Get a copy of the transactions
        List<Transaction> recentTransactions = new ArrayList<>(
                dataManager.getTransactions()
        );

        // Sort transactions by date, newest first
        recentTransactions.sort(
                Comparator.comparing(Transaction::getDate).reversed()
        );

        // Display up to five recent transactions
        if (recentTransactions.size() > 5) {

            recentTransactions = recentTransactions.subList(0, 5);
        }

        transactionsTable.setItems(
                FXCollections.observableArrayList(recentTransactions)
        );
    }


    // SCREEN NAVIGATION

    @FXML
    private void showDashboard() {

        // Refresh the dashboard information
        updateFinancialSummary();
        loadRecentTransactions();
    }


    @FXML
    private void showTransactions() {

        changeScreen("transaction-screen.fxml");
    }


    @FXML
    private void showBudgets() {

        changeScreen("budget-screen.fxml");
    }


    @FXML
    private void showReports() {

        changeScreen("report-screen.fxml");
    }


    // LOGOUT

    @FXML
    private void handleLogout() {
        try {
            BankingApplication.changeScene(
                    "login-screen.fxml",
                    dataManager,
                    null);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    // HELPER METHOD FOR SCREEN SWITCHING
    private void changeScreen(String fxmlFile) {

        try {
            BankingApplication.changeScene(
                    fxmlFile,
                    dataManager,
                    currentPerson);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}