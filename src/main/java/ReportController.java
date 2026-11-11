package main.java;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import main.java.model.SpendingReport;
import main.java.model.DataManager;
import main.java.model.Person;
import main.java.model.SpendingReport;
import java.time.LocalDate;
import java.util.Map;

public class ReportController {

    // Attributes from UML
    @FXML
    private BarChart<String, Number> spendingChart;

    private DataManager dataManager;
    private Person currentPerson;
    private SpendingReport spendingReport;

    // Used to remember the dates of the current report
    private LocalDate startDate;
    private LocalDate endDate;

    // UML: initialize(): void
    @FXML
    public void initialize() {
        if (spendingChart != null) {
            spendingChart.setTitle("Spending by Category");
            spendingChart.setLegendVisible(false);
        }
    }

    // UML: generateReport(startDate, endDate): SpendingReport
    public SpendingReport generateReport(
            LocalDate startDate, LocalDate endDate) {

        if (dataManager == null || startDate == null || endDate == null
                || endDate.isBefore(startDate)) {
            return null;
        }

        this.startDate = startDate;
        this.endDate = endDate;

        spendingReport = new SpendingReport(startDate, endDate);

        // Calculate total spending
        spendingReport.calculateTotal(dataManager.getTransactions());

        // Display the report
        displayChart(spendingReport);

        return spendingReport;

        this.startDate = startDate;
        this.endDate = endDate;

        // Create the report object
        spendingReport = new SpendingReport(startDate, endDate);

        // Give the report access to existing transactions
        spendingReport.setTransactions(dataManager.getTransactions());

        // Calculate the report information
        generateMonthlyBudgetReport(Budget budget, List<Transaction> transactions)

        // Display the report
        displayChart(spendingReport);

        return spendingReport;
    }

    // UML: displayChart(report: SpendingReport): void
    public void displayChart(SpendingReport report) {

        if (report == null || spendingChart == null) {
            return;
        }

        // Clear the previous chart
        spendingChart.getData().clear();

        // Create a new chart series
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // Get spending information by category
        Map<String, Double> spendingData = report.spendingByCategory(dataManager.getTransactions());

        // Add each category and its spending to the chart
        for (Map.Entry<String, Double> entry : spendingData.entrySet()) {

            series.getData().add(
                    new XYChart.Data<>(entry.getKey(), entry.getValue())
            );
        }

        spendingChart.getData().add(series);
    }

    // UML: refreshReport(): void
    public void refreshReport() {

        if (startDate != null && endDate != null) {
            generateReport(startDate, endDate);
        }
    }

    // Allows the application to provide the shared DataManager
    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    // Allows the application to provide the logged-in user
    public void setCurrentPerson(Person currentPerson) {
        this.currentPerson = currentPerson;
    }

    public Person getCurrentPerson() {
        return currentPerson;
    }
}
