package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.model.DataManager;
import main.java.model.Person;
import main.java.controller.MainScreenController;

public class BankingApplication extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {

        primaryStage = stage;

        // Load Login Screen
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/layouts/login-screen.fxml")
        );

        Parent root = loader.load();

        // Create Scene
        Scene scene = new Scene(root, 500, 650);

        // Configure Window
        primaryStage.setTitle("Rowdy Budgeteers");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    // Allows controllers to switch between screens
    public static void changeScene(String fxmlFile,
                                   DataManager dataManager,
                                   Person currentPerson) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                BankingApplication.class.getResource(
                        "/layouts/" + fxmlFile
                )
        );

        // Pass information to MainScreenController
        loader.setControllerFactory(controllerClass -> {

            if (controllerClass == MainScreenController.class) {
                return new MainScreenController(dataManager, currentPerson);
            }

            try {
                return controllerClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Parent root = loader.load();

        primaryStage.setScene(new Scene(root, 900, 700));
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}