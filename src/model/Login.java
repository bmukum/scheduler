package model;

/**
 * Login class
 */

/**
 *
 * @author Brandon Mukum
 */
import database.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.ZoneId;
import java.util.Locale;

public class Login extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/login.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 800, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        JDBC.makeConnection();

        launch(args);
    }
}
