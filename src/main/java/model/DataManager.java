package main.java.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private List<Person> users;
    private List<Budget> budgets;
    private List<Transaction> transactions;
//comment
    private String dataDirectory;

    public DataManager() {
        users = new ArrayList<>();
        budgets = new ArrayList<>();
        transactions = new ArrayList<>();

        dataDirectory = "data";

        File directory = new File(dataDirectory);

        if (!directory.exists()) {
            directory.mkdir();
        }

        loadUsers();
        loadBudgets();
        loadTransactions();
    }

    // CREATE SUPPORT

    public boolean addPerson(Person person) {
        if (person == null || !person.createPerson()) {
            return false;
        }

        if (findPerson(person.getUserName()) != null) {
            return false;
        }

        users.add(person);
        saveUsers();

        return true;
    }

    public boolean addBudget(Budget budget) {
        if (budget == null || !budget.validateBudget()) {
            return false;
        }

        if (findBudget(budget.getBudgetID()) != null) {
            return false;
        }

        budgets.add(budget);
        saveBudgets();

        return true;
    }

    public boolean addTransaction(Transaction transaction) {
        if (transaction == null || !transaction.validateTransaction()) {
            return false;
        }

        if (findTransaction(transaction.getTransactionID()) != null) {
            return false;
        }

        transactions.add(transaction);
        saveTransactions();

        return true;
    }

    // FIND METHODS

    public Person findPerson(String userName) {
        for (Person person : users) {
            if (person.getUserName().equals(userName)) {
                return person;
            }
        }

        return null;
    }

    public Budget findBudget(String budgetID) {
        for (Budget budget : budgets) {
            if (budget.getBudgetID().equals(budgetID)) {
                return budget;
            }
        }

        return null;
    }

    public Transaction findTransaction(String transactionID) {
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionID().equals(transactionID)) {
                return transaction;
            }
        }

        return null;
    }

    // SAVE METHODS

    public void saveUsers() {
        saveList(users, "users.dat");
    }

    public void saveBudgets() {
        saveList(budgets, "budgets.dat");
    }

    public void saveTransactions() {
        saveList(transactions, "transactions.dat");
    }

    private void saveList(List<?> list, String fileName) {
        try (ObjectOutputStream output = new ObjectOutputStream(
                new FileOutputStream(dataDirectory + "/" + fileName))) {

            output.writeObject(list);

        } catch (IOException e) {
            System.out.println("Error saving " + fileName + ": " + e.getMessage());
        }
    }

    // LOAD METHODS

    public void loadUsers() {
        users = loadList("users.dat");
    }

    public void loadBudgets() {
        budgets = loadList("budgets.dat");
    }

    public void loadTransactions() {
        transactions = loadList("transactions.dat");
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> loadList(String fileName) {
        File file = new File(dataDirectory + "/" + fileName);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream input = new ObjectInputStream(
                new FileInputStream(file))) {

            return (List<T>) input.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading " + fileName + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
