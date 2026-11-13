package main.java.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.ReadOnlyStringWrapper;

import main.java.BankingApplication;
import main.java.model.DataManager;
import main.java.model.Person;
import main.java.model.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


public class TransactionController {

    // Shared application data
    private DataManager dataManager;
    private Person currentPerson;


    // FXML COMPONENTS

    @FXML
    private javafx.scene.control.TextField purchaseNameField;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private javafx.scene.control.TextField amountField;

    @FXML
    private DatePicker purchaseDatePicker;

    @FXML
    private Label messageLabel;

    @FXML
    private TableView<Transaction> transactionsTable;

    @FXML
    private TableColumn<Transaction, String> transactionIDColumn;

    @FXML
    private TableColumn<Transaction, String> purchaseColumn;

    @FXML
    private TableColumn<Transaction, String> categoryColumn;

    @FXML
    private TableColumn<Transaction, String> dateColumn;

    @FXML
    private TableColumn<Transaction, String> amountColumn;


    // CONSTRUCTOR

    public TransactionController(DataManager dataManager,
                                 Person currentPerson) {

        this.dataManager = dataManager;
        this.currentPerson = currentPerson;
    }


    // INITIALIZE SCREEN

    @FXML
    public void initialize() {

        // Add available transaction categories
        categoryComboBox.setItems(
                FXCollections.observableArrayList(
                        "Food",
                        "Transportation",
                        "Shopping",
                        "Entertainment",
                        "Housing",
                        "Utilities",
                        "Healthcare",
                        "Other"));

        // Set up the transaction table
        setupTable();

        // Load existing transactions
        refreshTransactions();
    }


    // SET UP TABLE COLUMNS

    private void setupTable() {

        transactionIDColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getTransactionID()));

        purchaseColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getPurchaseName()));

        categoryColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getPurchaseType()));

        dateColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getDate().toString()));

        amountColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(String.format(Locale.US, "$%,.2f", cellData.getValue().getCost())));
    }


    // LOAD TRANSACTIONS

    private void refreshTransactions() {

        List<Transaction> transactionList = new ArrayList<>(
                dataManager.getTransactions()
        );

        // Display newest transactions first
        transactionList.sort(
                Comparator.comparing(Transaction::getDate).reversed()
        );

        transactionsTable.setItems(
                FXCollections.observableArrayList(transactionList)
        );
    }


    // ADD TRANSACTION

    @FXML
    private void handleAddTransaction() {

        String purchaseName = purchaseNameField.getText().trim();

        String category = categoryComboBox.getValue();

        String amountText = amountField.getText().trim();

        LocalDate purchaseDate = purchaseDatePicker.getValue();


        // Check for empty fields
        if (purchaseName.isEmpty()
                || category == null
                || amountText.isEmpty()
                || purchaseDate == null) {

            messageLabel.setText(
                    "Please complete all transaction fields."
            );

            return;
        }


        // Check that the amount is a valid number
        double amount;

        try {

            amount = Double.parseDouble(amountText);

        } catch (NumberFormatException e) {

            messageLabel.setText(
                    "Please enter a valid transaction amount."
            );

            return;
        }


        if (amount <= 0 || !Double.isFinite(amount)) {

            messageLabel.setText(
                    "The transaction amount must be greater than zero."
            );

            return;
        }


        // Generate a unique transaction ID

        // TRANSACTIONID should be something simple since the user has to type it to update/delete existing transaction.
        // same thing for budgets and reports. will discuss further

        String transactionID = UUID.randomUUID().toString();


        // Create the transaction
        Transaction transaction = new Transaction(
                transactionID,
                purchaseName,
                category,
                purchaseDate,
                amount
        );


        // Add the transaction to the DataManager
        if (dataManager.addTransaction(transaction)) {

            messageLabel.setText(
                    "Transaction added successfully!"
            );

            // Clear the input fields
            clearFields();

            // Refresh the table
            refreshTransactions();

        } else {

            messageLabel.setText(
                    "Unable to add transaction."
            );
        }
    }


    // UPDATE TRANSACTION

    @FXML
    private void handleUpdateTransaction() {

        Transaction selectedTransaction =
                transactionsTable.getSelectionModel().getSelectedItem();

        // Make sure a transaction is selected
        if (selectedTransaction == null) {

            messageLabel.setText(
                    "Please select a transaction to update."
            );

            return;
        }


        String amountText = amountField.getText().trim();

        if (amountText.isEmpty()) {

            messageLabel.setText(
                    "Enter the new amount in the Amount field."
            );

            return;
        }


        double newAmount;

        try {

            newAmount = Double.parseDouble(amountText);

        } catch (NumberFormatException e) {

            messageLabel.setText(
                    "Please enter a valid amount."
            );

            return;
        }


        if (newAmount <= 0 || !Double.isFinite(newAmount)) {

            messageLabel.setText(
                    "The amount must be greater than zero."
            );

            return;
        }


        // Update the selected transaction
        selectedTransaction.updateTransaction(newAmount);

        // Save the updated information
        dataManager.updateTransactions(selectedTransaction);

        messageLabel.setText(
                "Transaction updated successfully!"
        );

        // Clear the amount field and refresh the table
        amountField.clear();

        refreshTransactions();
    }


    // DELETE TRANSACTION

    @FXML
    private void handleDeleteTransaction() {

        Transaction selectedTransaction =
                transactionsTable.getSelectionModel().getSelectedItem();

        if (selectedTransaction == null) {
            messageLabel.setText("Please select a transaction to delete.");
            return;
        }

        if (dataManager.deleteTransaction(selectedTransaction.getTransactionID())) {
            messageLabel.setText("Transaction deleted successfully!");
            refreshTransactions();
        } else {
            messageLabel.setText("Unable to delete transaction.");
        }
    }



    // CLEAR INPUT FIELDS

    private void clearFields() {

        purchaseNameField.clear();

        categoryComboBox.getSelectionModel().clearSelection();

        amountField.clear();

        purchaseDatePicker.setValue(null);
    }


    // SCREEN NAVIGATION

    @FXML
    private void showDashboard() {

        changeScreen("main-screen.fxml");
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
                    null
            );

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
                    currentPerson
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}