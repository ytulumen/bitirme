package sample.AFewWorks;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import sample.Model.Voter;
import sample.NotDone.VoteScreenController;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
// TODO: make elections to be selectable
public class VoterPanelController implements Initializable {

    private static SessionFactory factory;
    private static ServiceRegistry serviceRegistry;
    private ActionEvent actionEvent;

    private Voter voter;

    @FXML
    TableView<Election> electionTable;

    @FXML
    TableColumn<Election, Integer> idColumn;

    @FXML
    TableColumn<Election, String> titleColumn;

    @FXML
    TableColumn<Election, String> descriptionColumn;

    private ObservableList<Election> observableElections = FXCollections.observableArrayList();

    public VoterPanelController() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(new PropertyValueFactory<Election, Integer>("electionID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<Election, String>("topic"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Election, String>("title"));
        observableElections.addAll(getElections());
        electionTable.setItems(observableElections);

    }

    @FXML
    public void selectElection(ActionEvent event)throws IOException {
        Election election = electionTable.getSelectionModel().getSelectedItem();

        FXMLLoader loader = null;
        Parent root = null;
        loader = new FXMLLoader(getClass().getClassLoader().getResource("fx/notdone/VoteScreen.fxml"));
        root = loader.load();
        VoteScreenController voteScreenController = loader.getController();
        voteScreenController.setVariables(election, voter);
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        primaryStage.setTitle("VoteScreen");
        primaryStage.setScene(new Scene(root, 750,700));
        primaryStage.show();
    }


    public void logout(ActionEvent event) throws IOException {
        this.actionEvent = event;
        loadScene("MainPage");
    }

    public void setVoterFromOutside(Voter voter){
        this.voter = voter;
    }
    public void loadVoter(){

    }
    private List<Election> getElections() {
        Configuration config = new Configuration();
        config.configure();
        config.addAnnotatedClass(Election.class);
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
        factory = config.buildSessionFactory(serviceRegistry);
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
            primaryStage.setTitle(page);
            primaryStage.setScene(new Scene(root, 750,700));
            primaryStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
