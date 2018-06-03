package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminPanelController implements Initializable {
    private ActionEvent actionEvent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void addCandidate(ActionEvent event){
        this.actionEvent = event;
        loadScene("AddCandidate");
    }
    @FXML
    public void addElection(ActionEvent event){
        this.actionEvent = event;
        loadScene("AddElection");
    }
    @FXML
    public void addVoter(ActionEvent event){
        this.actionEvent = event;
        loadScene("AddVoter");
    }
    @FXML
    public void showResults(ActionEvent event){
        this.actionEvent = event;
        loadScene("ShowResults");
    }
    @FXML
    public void showElections(ActionEvent event){
        this.actionEvent = event;
        loadScene("ShowElections");
    }
    @FXML
    public void showCandidates(ActionEvent event){
        this.actionEvent = event;
        loadScene("ShowCandidate");
    }
    @FXML
    public void showVoters(ActionEvent event){
        this.actionEvent = event;
        loadScene("ShowVoters");
    }
    @FXML
    public void logout(ActionEvent event) {
        this.actionEvent = event;
        loadScene("MainPage");
    }
    @FXML
    public void deleteElection(ActionEvent event){
        this.actionEvent = event;
        loadScene("DeleteElection");
    }
    @FXML
    public void deleteCandidate(ActionEvent event){
        this.actionEvent = event;
        loadScene("DeleteCandidate");
    }
    @FXML
    public void deleteVoter(ActionEvent event){
        this.actionEvent = event;
        loadScene("DeleteVoter");
    }
    @FXML
    public void editElection(ActionEvent event){
        this.actionEvent = event;
        loadScene("SelectEditableElection");
    }
    @FXML
    public void editCandidate(ActionEvent event){
        this.actionEvent = event;
        loadScene("SelectEditableCandidate");
    }
    @FXML
    public void editVoter(ActionEvent event){
        this.actionEvent = event;
        loadScene("SelectEditableVoter");
    }
    @FXML
    public void stopVoting(ActionEvent event){
        this.actionEvent = event;
        loadScene("StopVoting");
    }


    private void loadScene(String page) {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fx/" + page +".fxml"));
            Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            primaryStage.setTitle("Online Election System");
            primaryStage.setScene(new Scene(root, 750,700));
            primaryStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
