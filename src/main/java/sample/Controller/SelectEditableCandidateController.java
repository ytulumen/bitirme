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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SelectEditableCandidateController implements Initializable {
    private static SessionFactory factory;
    private static ServiceRegistry serviceRegistry;
    private ActionEvent actionEvent;

    @FXML
    private TextField candidateID;


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



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Configuration config = new Configuration();
        config.configure();
        config.addAnnotatedClass(Candidate.class);
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
        factory = config.buildSessionFactory(serviceRegistry);

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
    public void edit(ActionEvent event){
        boolean editFlag = true;
        List<Candidate> candidates = this.getCandidates();
        FXMLLoader loader = null;
        Parent root = null;

        for (Candidate candidate : candidates ) {
            if(candidate.getIdentityNumber() == Integer.parseInt(candidateID.getText())){
                editFlag = false;
                actionEvent = event;
                editCandidate(candidate.getId());
                break;
            }
        }
        if (editFlag){
            errorAlert();
        }
    }
    private void editCandidate(int id){
        FXMLLoader loader = null;
        Parent root = null;
        try {
            loader = new FXMLLoader(getClass().getClassLoader().getResource("fx/EditCandidate.fxml"));
            root = loader.load();
            EditCandidateController editCandidateController = loader.getController();
            editCandidateController.setCandidateIdFromOutside(id);
            editCandidateController.loadCandidate();
            Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            primaryStage.setTitle("Online Election System");
            primaryStage.setScene(new Scene(root, 750,700));
            primaryStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
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
            primaryStage.setTitle("Online Election System");
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
    private void errorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Database Error");
        alert.setContentText("There is an candidate with same id or check your database connections");
        alert.showAndWait();
    }
}
