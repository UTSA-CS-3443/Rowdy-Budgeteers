package main.java.model;

import java.io.Serializable;

public class Person implements Serializable {

    private String userName;
    private String password;
    private String email;

    public Person(String userName, String password, String email) {
        this.userName = userName;
        this.password = password;
        this.email = email;
    }

    // UML: createPerson(): boolean
    //check for null values, check for whitespace, check for empty stirng
    // make sure every condition is true
    // must return true or false
    public boolean createPerson() {
        return userName != null && !userName.trim().isEmpty()
                && password != null && !password.trim().isEmpty()
                && email != null && !email.trim().isEmpty();
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }
}
