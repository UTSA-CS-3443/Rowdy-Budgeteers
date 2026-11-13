package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;

import main.java.BankingApplication;
import main.java.model.DataManager;
import main.java.model.Person;

import java.io.File;
import java.io.FileNotFoundException;

public class LoginController {

    private DataManager dataManager;
    private Person currentPerson;

    // FXML COMPONENTS

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;


    // CONSTRUCTOR

    public LoginController() {
            try {
                dataManager = new DataManager();
                File usersFile = new File("data/users.csv");
                if (usersFile.exists()) {
                    dataManager.loadUsers("data/users.csv");
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Unable to load application data.", e);
            }
        }

    // LOGIN BUTTON

    @FXML
    private void handleLogin() {

        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        // Check for empty fields
        if (username.isBlank() || password.isBlank()) {

            messageLabel.setText(
                    "Please enter your username and password.");

            return;
        }
        // Attempt login
        if (login(username, password)) {

            try {
                dataManager.setLoggedInUser(currentPerson.getUserName());

                String transactionFileName = "data/transactions/" + currentPerson.getUserName() + "_transactions.csv";
                String budgetFileName = "data/budget/" + currentPerson.getUserName() + "_budget.csv";
                File txFile = new File(transactionFileName);
                File bxFile = new File(budgetFileName);
                if (txFile.exists()) {
                    dataManager.loadTransactions(transactionFileName);
                }
                if (bxFile.exists()){
                    dataManager.loadBudgets(budgetFileName);
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            messageLabel.setText("Login successful!");
            System.out.println(
                    "Welcome, " + currentPerson.getName() + "!");

            try {
                BankingApplication.changeScene("main-screen.fxml", dataManager, currentPerson);

            } catch (Exception e) {
                e.printStackTrace();
                messageLabel.setText("Unable to load the main screen.");
            }

        } else {

            messageLabel.setText(
                    "Invalid username or password.");
        }
    }
    // REGISTER

    public boolean register(String name, String email,
                            String userName, String password) {

        Person newPerson = new Person(
                name, email, userName, password);

        return dataManager.addPerson(newPerson);
    }
    // LOGIN LOGIC

    public boolean login(String userName, String password) {

        Person person = dataManager.findPerson(userName);

        if (person != null && person.authenticate(password)) {

            currentPerson = person;

            return true;
        }
        return false;
    }
    // LOGOUT

    public void logout() {

        currentPerson = null;
    }
    // GET CURRENT USER
    public Person getCurrentPerson() {
        return currentPerson;
    }

}