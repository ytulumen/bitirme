package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import sample.Model.Candidate;
import sample.Model.Election;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DeleteCandidateController implements Initializable {

    private static SessionFactory factory;
    private static ServiceRegistry serviceRegistry;
    private ActionEvent actionEvent;

    @FXML
    private TableView<Candidate> candidateTable;

    @FXML
    private TableColumn<Candidate, Integer> idColumn;

    @FXML
    private TableColumn<Candidate, String> nameColumn;

    @FXML
    private TableColumn<Candidate, String> surnameColumn;

    @FXML
    private TableColumn<Candidate, String> addressColumn;

    @FXML
    private TableColumn<Candidate, String> descriptionColumn;

    @FXML
    private TableColumn<Candidate, Integer> electionIdColumn;

    private ObservableList<Candidate> observableCandidate = FXCollections.observableArrayList();



    @FXML
    private TextField candidateID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(new PropertyValueFactory<Candidate, Integer>("identityNumber"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Candidate, String>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<Candidate, String>("surname"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<Candidate, String>("street"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Candidate, String>("description"));
        electionIdColumn.setCellValueFactory(new PropertyValueFactory<Candidate, Integer>("electionid"));

        observableCandidate.addAll(getCandidates());
        candidateTable.setItems(observableCandidate);
    }
    @FXML
    public void delete(ActionEvent event){

        boolean emptyFlag = true;
        if(candidateID.getText() == null || candidateID.getText().trim().isEmpty()){
            emptyFlag = false;
            candidateID.getStyleClass().remove("best");
            candidateID.getStyleClass().add("error");
        }else {
            candidateID.getStyleClass().add("best");
        }
        if (emptyFlag){
            boolean deleteFlag = true;
            try {
                List<Candidate> candidates = this.getCandidates();

                for (Candidate candidate : candidates ) {
                    if(candidate.getIdentityNumber() == Integer.parseInt(candidateID.getText())){
                        deleteFlag = false;
                        successfulAlert();
                        deleteCandidate();
                        back(event);
                    }
                }
                if (deleteFlag){
                    errorAlert("There is an candidate with " + candidateID.getText()
                            + " id or check your database connections");
                    candidateID.getStyleClass().remove("best");
                    candidateID.getStyleClass().add("error");
                }
            }catch (NumberFormatException e){
                e.printStackTrace();
                candidateID.getStyleClass().remove("best");
                candidateID.getStyleClass().add("error");
                errorAlert("Invalid candidate id " + candidateID.getText()
                        + " or check your database connections");
            }
        }
        else {
            errorAlert("Cannot be null");
        }
    }
    private int deleteCandidate(){

        Session sesn = factory.openSession();
        Transaction tx;
        int result = 0;
        try {
            tx = sesn.beginTransaction();
            result = sesn.createQuery("delete Candidate where identityNumber = '" +
                    Integer.parseInt(candidateID.getText()) + "'" ).executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            sesn.close();
        }
        return result;
    }



    @FXML
    public void logout(ActionEvent event) {
        this.actionEvent = event;
        loadScene("MainPage");
    }
    @FXML
    public void back(ActionEvent event) {
        this.actionEvent = event;
        loadScene("AdminPanel");
    }
    private void loadScene(String page) {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fx/" + page +".fxml"));
            Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            primaryStage.setTitle(page);
            primaryStage.setScene(new Scene(root, 750,700));
            primaryStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private List<Candidate> getCandidates() {
        Configuration config = new Configuration();
        config.configure();
        config.addAnnotatedClass(Candidate.class);
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
        factory = config.buildSessionFactory(serviceRegistry);
        Session sesn = factory.openSession();
        Transaction tx = null;
        List<Candidate> candidates = new ArrayList<>();
        try {
            tx = sesn.beginTransaction();
            candidates = (List) sesn.createQuery("from Candidate").list();
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            sesn.close();
        }

        return candidates;
    }
    private void errorAlert(String errorString) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Database Error");
        alert.setContentText(errorString);
        alert.showAndWait();
    }
    private void successfulAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("INFORMATION");
        alert.setHeaderText("Deleted");
        alert.setContentText("Candidate deleted successfully");
        alert.showAndWait();
    }

}
