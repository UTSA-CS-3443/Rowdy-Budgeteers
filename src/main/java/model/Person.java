package main.java.model;

import java.io.Serializable;

public class Person implements Serializable {
    private String name;
    private String email;
    private String userName;
    private String password;

    public Person(String name, String email, String userNane, String password) {
        this.name = name;
        this.email = email;
        this.userName = userName;
        this.password = password;
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
    public String getName() {
        return name;
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

    public void setName(String name) {
        this.name = name;
    }
    public void setUserName(String password){
        this.userName = userName;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }
}
