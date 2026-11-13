package main.java.model;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.nio.file.Files;

public class DataManager {

    private List<Person> users;
    private List<Budget> budgets;
    private List<Transaction> transactions;
    //comment
    private String dataDirectory;
    String loggedInUser = "Test";

    public DataManager() throws FileNotFoundException {
        users = new ArrayList<>();
        budgets = new ArrayList<>();
        transactions = new ArrayList<>();
        dataDirectory = "data";

        new File(dataDirectory).mkdirs();
        new File(dataDirectory + "/budget").mkdirs();
        new File(dataDirectory + "/transactions").mkdirs();
    }

    public void setLoggedInUser(String loggedInUser) {
        this.loggedInUser = loggedInUser;
    }


    //USER METHODS COMPLETE
    public List<Person> getUsers() {
        return users;
    }
    public boolean addPerson(Person person) {
        if (person == null || !person.createPerson()) return false;
        if (findPerson(person.getUserName()) != null) return false;
        users.add(person);
        return writeUsersToFile();
    }
    public Person findPerson(String userName) {
        for (Person person : users) {
            if (person.getUserName().equals(userName)) return person;
        }
        return null;
    }
    public boolean deleteUser(String userName){
        if(userName == null || userName.isEmpty()) return false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserName().equals(userName)) {
                users.remove(i);
                return writeUsersToFile();
            }
        }
        return false;

    }
    public boolean updateUser(Person updatedUser){
        if (users == null || updatedUser == null) return false;
        for (int i = 0; i < users.size(); i++){
            if (users.get(i).getUserName().equals(updatedUser.getUserName())){
                users.set(i, updatedUser);
                return writeUsersToFile();
            }
        }
        return false;
    }
    public void loadUsers(String fileName) throws FileNotFoundException {
        users.clear();
        File user = new File(fileName);
        Scanner scan = new Scanner(user);
        if (scan.hasNextLine()) {
            scan.nextLine(); // Skip the header/blank first line
        }
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            Person person = convertLineToUser(line);
            if (person != null) {
                users.add(person);
            }
        }
        scan.close();
    }
    private Person convertLineToUser(String line){
        if (line.trim().isEmpty()) {
            return null;
        }
        String[] tokens = line.split(",");
        if (tokens.length != 4){
            return null;
        }
        String userName = tokens[0];
        String userEmail = tokens[1];
        String userUserName= tokens[2];
        String userPassword = tokens[3];
        return new Person(userName, userEmail, userUserName, userPassword);
    }
    public boolean writeUsersToFile(){
        String filePath = "data/users.csv";
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))){
            writer.write("");
            writer.newLine();
            for (Person p : users){
                writer.write(p.toCSVString());
                writer.newLine();
            }
            return true;
        } catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    //BUDGET METHODS COMPLETE
    public List<Budget> getBudgets() {
        return budgets;
    }
    public boolean addBudget(Budget budget) {
        if (budget == null || !budget.validateBudget()) return false;
        if (findBudget(budget.getBudgetID()) != null) return false;
        budgets.add(budget);
        return writeBudgetToFile();
    }
    public Budget findBudget(String budgetID) {
        for (Budget budget : budgets) {
            if (budget.getBudgetID().equals(budgetID)) {
                return budget;
            }
        }
        return null;
    }
    public boolean deleteBudget(String budgetID){
        if (budgetID == null || budgetID.isEmpty()) return false;
        for (int i = 0; i < budgets.size(); i++) {
            if (budgets.get(i).getBudgetID().equals(budgetID)) {
                budgets.remove(i);
                return writeBudgetToFile();
            }
        }
        return false;
    }
    public boolean updateBudget(Budget updatedBudget){
        if (budgets == null || updatedBudget == null) return false;
        for (int i = 0; i < budgets.size(); i++){
            if (budgets.get(i).getBudgetID().equals(updatedBudget.getBudgetID())){
                budgets.set(i, updatedBudget);
                return writeTransactionsToFile();
            }
        }
        return false;
    }
    public void loadBudgets(String fileName) throws FileNotFoundException {
        budgets.clear();
        File userBudget = new File(fileName);
        Scanner scan = new Scanner(userBudget);
        if (scan.hasNextLine()) {
            scan.nextLine(); // Skip the header/blank first line
        }
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            Budget budget = convertLineToBudget(line);
            if (budget != null) {
                budgets.add(budget);
            }
        }
        scan.close();
    }
    private Budget convertLineToBudget(String line){
        if (line.trim().isEmpty()) {
            return null;
        }
        String[] tokens = line.split(",");
        if (tokens.length != 5){
            return null;
        }
        String budgetID = tokens[0].trim();
        String budgetType = tokens[1].trim();
        double budgetLimit;
        LocalDate startDate;
        LocalDate endDate;
        try {
            budgetLimit = Double.parseDouble(tokens[2].trim());
            startDate = LocalDate.parse(tokens[3].trim());
            endDate = LocalDate.parse(tokens[4].trim());
        } catch (NumberFormatException | DateTimeParseException e) {
            return null;
        }
        return new Budget(budgetID, budgetType, budgetLimit, startDate, endDate);
    }
    public boolean writeBudgetToFile(){
        String filePath = "data/budget/" + loggedInUser + "_budget.csv";
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))){
            writer.write("(String budgetID, String budgetType, double budgetLimit, LocalDate startDate, LocalDate endDate)");
            writer.newLine();
            for (Budget b : budgets){
                writer.write(b.toCSVString());
                writer.newLine();
            }
            return true;
        } catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }


    // TRANSACTION METHODS COMPLETE
    public List<Transaction> getTransactions() {
        return transactions;
    }
    public boolean addTransaction(Transaction transaction) {
        if (transaction == null || !transaction.validateTransaction()) return false;
        if (findTransaction(transaction.getTransactionID()) != null) return false;
        transactions.add(transaction);
        return writeTransactionsToFile();
    }
    public Transaction findTransaction(String transactionID) {
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionID().equals(transactionID)) return transaction;
        }
        return null;
    }
    public boolean deleteTransaction(String transactionID) {
        if (transactionID == null || transactionID.isEmpty()) return false;
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getTransactionID().equals(transactionID)) {
                transactions.remove(i);
                return writeTransactionsToFile();
            }
        }
        return false;
    }
    public boolean updateTransactions(Transaction updatedTransaction) {
        if (transactions == null || updatedTransaction == null) return false;
        for (int i = 0; i < transactions.size(); i++){
            if (transactions.get(i).getTransactionID().equals(updatedTransaction.getTransactionID())){
                transactions.set(i, updatedTransaction);
                return writeTransactionsToFile();
            }
        }
        return false;
    }
    public void loadTransactions(String fileName) throws FileNotFoundException {
        transactions.clear();
        File userTransactions = new File(fileName);
        Scanner scan = new Scanner(userTransactions);
        if (scan.hasNextLine()) {
            scan.nextLine();
        }
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            Transaction transaction = convertLineToTransaction(line);
            if (transaction != null) {
                transactions.add(transaction);
            }
        }
        scan.close();
    }
    private Transaction convertLineToTransaction(String line){
        if (line.trim().isEmpty()) {
            return null;
        }
        String[] tokens = line.split(",");
        if (tokens.length != 5){
            return null;
        }
        String transactionID = tokens[0];
        String transactionName = tokens[1];
        String transactionType = tokens[2];
        LocalDate transactionDate;
        double transactionCost;
        try {
            transactionDate = LocalDate.parse(tokens[3].trim());
            transactionCost = Double.parseDouble(tokens[4].trim());
        } catch (NumberFormatException e) {
            return null;
        }
        return new Transaction(transactionID, transactionName, transactionType, transactionDate, transactionCost);
    }
    public boolean writeTransactionsToFile(){
        String filePath = "data/transactions/" + loggedInUser + "_transactions.csv";
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))){
            writer.write("(String transactionID, String purchaseName, String purchaseType, LocalDate date, double cost)");
            writer.newLine();
            for (Transaction t : transactions){
                writer.write(t.toCSVString());
                writer.newLine();
            }
            return true;
        } catch(IOException e){
            e.printStackTrace();
            return false;
        }
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
