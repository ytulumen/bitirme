package sample.NotDone;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Model.Election;
import sample.Model.Voter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class VoteScreenController implements Initializable {

    private Election election;
    private Voter voter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void logout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fx/MainPage.fxml"));
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        primaryStage.setTitle("Main Panel");
        primaryStage.setScene(new Scene(root, 800,600));
        primaryStage.show();
    }
    public void setVariables(Election election, Voter voter){
        this.election = election;
        this.voter = voter;
    }
}
