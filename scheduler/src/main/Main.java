package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Locale;
import java.util.Objects;

/**
 * The main class to get the application started
 * @author Derek Brown
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        //uncomment the code below to test language
        //Locale.setDefault(new Locale("fr"));

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Login.fxml")));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 375, 250));
        primaryStage.show();
    }

    /**
     * This is the main method. This launches the java program.
     * Javadocs folder is located in "scheduler/javadocs".
     * @param args for what it's worth
     */
    public static void main(String[] args){
        launch(args);
    }
}
