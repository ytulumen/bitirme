package sample;

import arduino.Arduino;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Model.FingerPrintSensor;

import java.util.Scanner;

import static java.lang.System.exit;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fx/MainPage.fxml"));
        primaryStage.setTitle("AddCandidate");
        primaryStage.setScene(new Scene(root, 800,600));
        primaryStage.show();
    }


    public static void main(String[] args) {
        //launch(args);





    }
}
