package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ScreenPreviewTester extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        String screen = "/layouts/login-screen.fxml";

        var resource = getClass().getResource(screen);

        // Debugging
        System.out.println("Looking for: " + screen);
        System.out.println("Resource found: " + resource);

        if (resource == null) {
            throw new IllegalStateException(
                    "FXML file could not be found! Check your resources folder."
            );
        }

        Parent root = FXMLLoader.load(resource);

        Scene scene = new Scene(root, 1100, 750);

        stage.setTitle("Rowdy Budgeteers");
        stage.setScene(scene);
        stage.show();
    }
}
