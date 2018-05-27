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
import sample.Model.Voter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SelectEditableVoterController implements Initializable {
    private static SessionFactory factory;
    private static ServiceRegistry serviceRegistry;
    private ActionEvent actionEvent;

    @FXML
    private TextField voterID;


    @FXML
    private TableView<Voter> voterTable;

    @FXML
    private TableColumn<Voter, Integer> idColumn;

    @FXML
    private TableColumn<Voter, String> nameColumn;

    @FXML
    private TableColumn<Voter, String> surnameColumn;

    @FXML
    private TableColumn<Voter, String> addressColumn;

    private ObservableList<Voter> observableVoter = FXCollections.observableArrayList();



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Configuration config = new Configuration();
        config.configure();
        config.addAnnotatedClass(Voter.class);
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
        factory = config.buildSessionFactory(serviceRegistry);

        idColumn.setCellValueFactory(new PropertyValueFactory<Voter, Integer>("identityNumber"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Voter, String>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<Voter, String>("surname"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<Voter, String>("street"));

        observableVoter.addAll(getVoters());
        voterTable.setItems(observableVoter);
    }
    @FXML
    public void edit(ActionEvent event){
        boolean editFlag = true;
        List<Voter> voters = this.getVoters();
        FXMLLoader loader = null;
        Parent root = null;

        for (Voter voter : voters ) {
            if(voter.getIdentityNumber() == Integer.parseInt(voterID.getText())){
                editFlag = false;
                actionEvent = event;
                editVoter(voter.getId());
                break;
            }
        }
        if (editFlag){
            errorAlert();
        }
    }
    private void editVoter(int id){
        FXMLLoader loader = null;
        Parent root = null;
        try {
            loader = new FXMLLoader(getClass().getClassLoader().getResource("fx/EditVoter.fxml"));
            root = loader.load();
            EditVoterController editVoterController = loader.getController();
            editVoterController.setVoterIdFromOutside(id);
            editVoterController.loadVoter();
            Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            primaryStage.setTitle("EditVoter");
            primaryStage.setScene(new Scene(root, 800,600));
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
            primaryStage.setTitle(page);
            primaryStage.setScene(new Scene(root, 800,600));
            primaryStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private List<Voter> getVoters() {
        Configuration config = new Configuration();
        config.configure();
        config.addAnnotatedClass(Voter.class);
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
        factory = config.buildSessionFactory(serviceRegistry);
        Session sesn = factory.openSession();
        Transaction tx = null;
        List<Voter> voters = new ArrayList<>();
        try {
            tx = sesn.beginTransaction();
            voters = (List) sesn.createQuery("from Voter").list();
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            sesn.close();
        }

        return voters;
    }
    private void errorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Database Error");
        alert.setContentText("There is an voter with same id or check your database connections");
        alert.showAndWait();
    }
}
