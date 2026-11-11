package main.java.view;

import main.java.model.DataManager;
import main.java.model.Person;

public class MainScreenController {

    private DataManager dataManager;
    private Person currentPerson;

    public MainScreenController (DataManager dataManager, Person currentPerson){
        this.dataManager = dataManager;
        this.currentPerson = currentPerson;
    }

    public void initialize() {

    }

}
