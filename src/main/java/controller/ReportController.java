package main.java.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import main.java.BankingApplication;
import main.java.model.DataManager;
import main.java.model.Person;
import main.java.model.SpendingReport;
import main.java.model.Transaction;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReportController {

    private DataManager dataManager;
    private Person currentPerson;

    @FXML private ComboBox<String> periodComboBox;

    @FXML private Label totalIncomeLabel;
    @FXML private Label totalExpensesLabel;
    @FXML private Label netSavingsLabel;

    @FXML private TableView<Map.Entry<String, Double>> spendingTable;
    @FXML private TableColumn<Map.Entry<String, Double>, String> categoryColumn;
    @FXML private TableColumn<Map.Entry<String, Double>, String> totalSpentColumn;
    @FXML private TableColumn<Map.Entry<String, Double>, String> percentageColumn;

    public ReportController(DataManager dataManager, Person currentPerson) {
        this.dataManager = dataManager;
        this.currentPerson = currentPerson;
    }

    @FXML
    public void initialize() {

        // Add the available report periods.
        periodComboBox.setItems(FXCollections.observableArrayList(
                "This Month",
                "Last Month",
                "Last 3 Months",
                "This Year",
                "All Time"
        ));

        // Select the current month by default.
        periodComboBox.setValue("This Month");

        setupTable();
        refreshReport();
    }

    // Connects the table columns to the spending information.
    private void setupTable() {

        spendingTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY);

        categoryColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(
                        cellData.getValue().getKey()));

        totalSpentColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(String.format(
                        Locale.US, "$%,.2f",
                        cellData.getValue().getValue())));

        percentageColumn.setCellValueFactory(cellData -> {

            double total = calculateTotalSpending();

            double percentage = total > 0
                    ? cellData.getValue().getValue() / total * 100
                    : 0;

            return new ReadOnlyStringWrapper(String.format(
                    Locale.US, "%.1f%%", percentage));
        });
    }

    // Calculates the date range based on the selected report period.
    private LocalDate[] getDateRange() {

        LocalDate today = LocalDate.now();
        LocalDate startDate;
        LocalDate endDate = today;

        String selectedPeriod = periodComboBox.getValue();

        if ("Last Month".equals(selectedPeriod)) {

            YearMonth lastMonth = YearMonth.now().minusMonths(1);

            startDate = lastMonth.atDay(1);
            endDate = lastMonth.atEndOfMonth();

        } else if ("Last 3 Months".equals(selectedPeriod)) {

            startDate = today.minusMonths(2).withDayOfMonth(1);

        } else if ("This Year".equals(selectedPeriod)) {

            startDate = LocalDate.of(today.getYear(), 1, 1);

        } else if ("All Time".equals(selectedPeriod)) {

            startDate = LocalDate.of(1900, 1, 1);

        } else {

            // Default to the current month.
            startDate = today.withDayOfMonth(1);
        }

        return new LocalDate[]{startDate, endDate};
    }

    // Calculates total spending for the selected date range.
    private double calculateTotalSpending() {

        LocalDate[] dates = getDateRange();

        SpendingReport report = new SpendingReport(
                dates[0], dates[1]);

        return report.calculateTotal(dataManager.getTransactions());
    }

    // Updates the financial summary and category breakdown.
    private void refreshReport() {

        LocalDate[] dates = getDateRange();

        SpendingReport report = new SpendingReport(
                dates[0], dates[1]);

        List<Transaction> transactions =
                dataManager.getTransactions();

        double totalSpending = report.calculateTotal(transactions);

        // Income is not currently stored by the Transaction model.
        totalIncomeLabel.setText("Not tracked");

        totalExpensesLabel.setText(String.format(
                Locale.US, "$%,.2f", totalSpending));

        // Net savings cannot be calculated without income data.
        netSavingsLabel.setText("Not tracked");

        // Get the total spending for each category.
        Map<String, Double> categorySpending =
                report.spendingByCategory(transactions);

        List<Map.Entry<String, Double>> entries =
                new ArrayList<>(categorySpending.entrySet());

        // Display the categories with the highest spending first.
        entries.sort(Map.Entry.<String, Double>comparingByValue()
                .reversed());

        spendingTable.setItems(
                FXCollections.observableArrayList(entries));
    }

    // Runs whenever the user selects a different report period.
    @FXML
    private void handlePeriodChange() {
        refreshReport();
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
    private void showBudgets() {
        changeScreen("budget-screen.fxml");
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