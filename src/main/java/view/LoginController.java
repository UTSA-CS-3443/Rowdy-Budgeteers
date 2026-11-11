package main.java.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import main.java.model.DataManager;
import main.java.model.Person;

public class LoginController {

    private DataManager dataManager;
    private Person currentPerson;

    // FXML components
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    // Constructor
    public LoginController() {
        dataManager = new DataManager();
    }

    // LOGIN BUTTON
    @FXML
    private void handleLogin() {

        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username == null || username.isBlank()
                || password == null || password.isBlank()) {

            messageLabel.setText("Please enter your username and password.");
            return;
        }

        if (login(username, password)) {

            messageLabel.setText("Login successful!");

            // TODO: Navigate to dashboard

        } else {

            messageLabel.setText("Invalid username or password.");
        }
    }

    // REGISTER
    public boolean register(String userName, String password, String email) {

        Person newPerson = new Person(userName, password, email);

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