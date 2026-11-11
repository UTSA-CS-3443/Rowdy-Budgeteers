package main.java.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;

import main.java.BankingApplication;
import main.java.model.DataManager;
import main.java.model.Person;
import main.java.model.Transaction;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;

public class MainScreenController {

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

        // Display user's name
        if (currentPerson != null) {
            welcomeLabel.setText(
                    "Welcome Back, " + currentPerson.getName() + "!"
            );
        }

        // Configure transaction table
        setupTransactionTable();

        // Update financial information
        updateDashboard();
    }


    // SETUP TRANSACTION TABLE

    private void setupTransactionTable() {

        dateColumn.setCellValueFactory(
                cell -> new SimpleStringProperty(
                        cell.getValue().getDate()
                )
        );

        categoryColumn.setCellValueFactory(
                cell -> new SimpleStringProperty(
                        cell.getValue().getPurchaseType()
                )
        );

        amountColumn.setCellValueFactory(
                cell -> new SimpleStringProperty(
                        String.format("$%.2f",
                                cell.getValue().getCost())
                )
        );

        descriptionColumn.setCellValueFactory(
                cell -> new SimpleStringProperty(
                        cell.getValue().getPurchaseName()
                )
        );

    }


    // UPDATE DASHBOARD

    private void updateDashboard() {

        if (dataManager == null) {
            return;
        }

        // Display available financial information
        totalBalanceLabel.setText("Not available");

        activeBudgetsLabel.setText("Not available");

        // Calculate monthly spending
        double monthlySpending = calculateMonthlySpending();

        monthlySpendingLabel.setText(
                String.format("$%.2f", monthlySpending)
        );

        // Populate recent transactions
        transactionsTable.getItems().setAll(
                dataManager.getTransactions()
        );
    }


    // CALCULATE MONTHLY SPENDING

    private double calculateMonthlySpending() {

        double total = 0.0;

        if (dataManager.getTransactions() == null) {
            return total;
        }

        YearMonth currentMonth = YearMonth.now();

        for (Transaction transaction :
                dataManager.getTransactions()) {

            try {

                LocalDate transactionDate =
                        LocalDate.parse(transaction.getDate());

                if (YearMonth.from(transactionDate)
                        .equals(currentMonth)) {

                    total += transaction.getCost();
                }

            } catch (DateTimeParseException e) {

                // Ignore transactions with invalid date formats
                continue;
            }
        }

        return total;
    }


    // NAVIGATION BUTTONS

    @FXML
    private void showDashboard() {

        updateDashboard();
    }


    @FXML
    private void showTransactions() {

        try {

            BankingApplication.changeScene(
                    "transaction-screen.fxml"
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    @FXML
    private void showBudgets() {

        try {

            BankingApplication.changeScene(
                    "budget-screen.fxml"
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    @FXML
    private void showReports() {

        try {

            BankingApplication.changeScene(
                    "report-screen.fxml"
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    // LOGOUT

    @FXML
    private void handleLogout() {

        try {

            BankingApplication.changeScene(
                    "login-screen.fxml"
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    // GET CURRENT PERSON

    public Person getCurrentPerson() {

        return currentPerson;
    }

}