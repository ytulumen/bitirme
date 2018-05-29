package sample.NotDone;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ShowResultsController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void logout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fx/MainPage.fxml"));
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        primaryStage.setTitle("Main Panel");
        primaryStage.setScene(new Scene(root, 750,700));
        primaryStage.show();
    }
}
