package main.java.model;

public class LoginController {

    private DataManager dataManager;
    private Person currentPerson;

    public LoginController(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public boolean register(String name, String userName, String password, String email) {

        Person newPerson = new Person(name, userName, password, email);

        if (!newPerson.createPerson()) {
            return false;
        }

        return dataManager.addPerson(newPerson);
    }

    public boolean login(String userName, String password) {

        Person person = dataManager.findPerson(userName);

        if (person != null && person.authenticate(password)) {
            currentPerson = person;
            return true;
        }

        return false;
    }

    public void logout() {
        currentPerson = null;
    }

    public Person getCurrentPerson() {
        return currentPerson;
    }
}