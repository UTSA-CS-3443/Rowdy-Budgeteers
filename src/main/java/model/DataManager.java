package main.java.model;

import java.io.*;
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

        File directory = new File(dataDirectory);

        if (!directory.exists()) {
            directory.mkdir();
        }
    }


    public void loadUsers() {
        users = new ArrayList<>();

        try (Scanner scan = new Scanner(
                new File(dataDirectory + "/users.csv"))) {

            while (scan.hasNextLine()) {
                String[] data = scan.nextLine().split(",");

                if (data.length == 4) {
                    users.add(new Person(
                            data[0], data[1], data[2], data[3]
                    ));
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Users file not found.");
        }
    }




    //USER METHODS HERE - CURRENTLY WORKING ON
    public List<Person> getUsers() {
        return users;
    }
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
    public Person findPerson(String userName) {
        for (Person person : users) {
            if (person.getUserName().equals(userName)) {
                return person;
            }
        }

        return null;
    }
    //public boolean deleteUser(String userName)
    //public boolean updateUser(Person updatedPerson)
    //public void loadUsers
    //private Transaction convertLineToUser(String line)
    //public boolean writeUserToFile()

    //BUDGET METHODS HERE - HAVEN'T STARTED
    public List<Budget> getBudgets() {
        return budgets;
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
    public Budget findBudget(String budgetID) {
        for (Budget budget : budgets) {
            if (budget.getBudgetID().equals(budgetID)) {
                return budget;
            }
        }

        return null;
    }
    //public boolean deleteBudget(String budgetID)
    //public boolean updateBudget(Transaction updatedBudget)
    //public void loadBudgets()
    //private Transaction convertLineToBudget(String line)
    //public boolean writeBudgetToFile()


    // TRANSACTION METHODS COMPLETE
    public List<Transaction> getTransactions() {
        return transactions;
    }
    public boolean addTransaction(Transaction transaction) {
        if (transaction == null || !transaction.validateTransaction()) {
            return false;
        }

        if (findTransaction(transaction.getTransactionID()) != null) {
            return false;
        }

        transactions.add(transaction);

        return writeTransactionsToFile();
    }
    public Transaction findTransaction(String transactionID) {
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionID().equals(transactionID)) {
                return transaction;
            }
        }
        return null;
    }
    public boolean deleteTransaction(String transactionID) {
        if (transactionID == null || transactionID.isEmpty()) {
            return false;
        }
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
        File userTransactions = new File(fileName);

        Scanner scan = new Scanner(userTransactions);
        if (scan.hasNextLine()){
            scan.hasNextLine();
        }
        while(scan.hasNextLine()){
            String line = scan.nextLine();
            Transaction transaction = convertLineToTransaction(line);
            if (transaction != null){
                boolean t = addTransaction(transaction);
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
        String transactionDate = tokens[3];
        double transactionCost;
        try {
            transactionCost = Double.parseDouble(tokens[4].trim());
        } catch (NumberFormatException e) {
            return null;
        }
        return new Transaction(transactionID, transactionName, transactionType, transactionDate, transactionCost);
    }
    public boolean writeTransactionsToFile(){
        String filePath = "data/transactions/" + loggedInUser + "_transactions.csv";
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))){
            writer.write("");
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
