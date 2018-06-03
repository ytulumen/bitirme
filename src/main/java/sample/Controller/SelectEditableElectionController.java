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
import sample.Model.Election;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SelectEditableElectionController implements Initializable {
    private static SessionFactory factory;
    private static ServiceRegistry serviceRegistry;
    private ActionEvent actionEvent;

    @FXML
    private TextField electionID;

    @FXML
    private TableView<Election> electionTable;

    @FXML
    private TableColumn<Election, Integer> idColumn;

    @FXML
    private TableColumn<Election, String> titleColumn;

    @FXML
    private TableColumn<Election, String> descriptionColumn;

    @FXML
    private TableColumn<Election, Boolean> votingColumn;

    private ObservableList<Election> observableElections = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Configuration config = new Configuration();
        config.configure();
        config.addAnnotatedClass(Election.class);
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
        factory = config.buildSessionFactory(serviceRegistry);

        idColumn.setCellValueFactory(new PropertyValueFactory<Election, Integer>("electionID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<Election, String>("topic"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Election, String>("title"));
        votingColumn.setCellValueFactory(new PropertyValueFactory<Election, Boolean>("isVotable"));
        observableElections.addAll(getElections());
        electionTable.setItems(observableElections);
    }
    @FXML
    public void edit(ActionEvent event){
        boolean editFlag = true;
        List<Election> elections = this.getElections();
        FXMLLoader loader = null;
        Parent root = null;

        for (Election election : elections ) {
            if(election.getElectionID() == Integer.parseInt(electionID.getText())){
                editFlag = false;
                actionEvent = event;
                editElection(election.getId());
                break;
            }
        }
        if (editFlag){
            errorAlert("There is any election with same id or check your database connections");
        }
    }
    private void editElection(int id){
        FXMLLoader loader = null;
        Parent root = null;
        try {
            loader = new FXMLLoader(getClass().getClassLoader().getResource("fx/EditElection.fxml"));
            root = loader.load();
            EditElectionController editElectionController = loader.getController();
            editElectionController.setElectionIdFromOutside(id);
            editElectionController.loadElection();
            Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            primaryStage.setTitle("Online Election System");
            primaryStage.setScene(new Scene(root, 750,700));
            primaryStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        this.actionEvent = event;
        loadScene("MainPage");
    }
    @FXML
    public void back(ActionEvent event) {
        this.actionEvent = event;
        loadScene("AdminPanel");
    }
    private List<Election> getElections() {

        Session sesn = factory.openSession();
        Transaction tx = null;
        List<Election> elections = new ArrayList<>();
        try {
            tx = sesn.beginTransaction();
            elections = (List) sesn.createQuery("from Election").list();
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            sesn.close();
        }

        return elections;
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
    private void errorAlert(String errorString) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Database Error");
        alert.setContentText(errorString);
        alert.showAndWait();
    }
}
