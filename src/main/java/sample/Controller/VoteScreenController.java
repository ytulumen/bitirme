package sample.Controller;

import javafx.beans.value.ObservableValue;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import sample.Controller.VoterPanelController;
import sample.Model.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static java.lang.System.exit;

public class VoteScreenController implements Initializable {

    private Election election;
    private Voter voter;
    private static SessionFactory factory;
    private static ServiceRegistry serviceRegistry;
    private ActionEvent actionEvent;
    private List<Candidate> candidates;
    private Candidate candidate;

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

/*    @FXML
    private TableColumn<Candidate, Integer> electionIdColumn;*/

    @FXML
    private TableColumn candidateImageTableColumn;

    private ObservableList<Candidate> observableCandidate = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    @FXML
    public void logout(ActionEvent event) throws IOException {
        this.actionEvent = event;
        loadScene("MainPage");
    }
    @FXML
    public void back(ActionEvent event) {
        this.actionEvent = event;
        loadScene("VoterPanel");
    }
    public void setVariables(Election election, Voter voter){
        this.election = election;
        this.voter = voter;
        candidates = getCandidates();
        idColumn.setCellValueFactory(new PropertyValueFactory<Candidate, Integer>("identityNumber"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Candidate, String>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<Candidate, String>("surname"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<Candidate, String>("street"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Candidate, String>("description"));
//        electionIdColumn.setCellValueFactory(new PropertyValueFactory<Candidate, Integer>("electionid"));
        //TODO:show IMAGEEEE
        observableCandidate.addAll(getCandidates());
        candidateTable.setItems(observableCandidate);
        candidateTable.setRowFactory(tv -> {
            TableRow<Candidate> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Candidate rowData = row.getItem();
                    candidate = rowData;
                    voteAlert();
                    if(searchForVoter() == voter.getFingerPrintID()){
                        voteCandidate(candidate);
                        addVoterElection();
                        try {
                            FXMLLoader loader = null;
                            Parent root = null;
                            loader = new FXMLLoader(getClass().getClassLoader().getResource("fx/VoterPanel.fxml"));
                            root = loader.load();
                            VoterPanelController voterPanelController = loader.getController();
                            voterPanelController.setVoterFromOutside(voter);
                            voterPanelController.loadVoter();
                            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            primaryStage.setTitle("Online Election System");
                            primaryStage.setScene(new Scene(root, 750,700));
                            primaryStage.show();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    else{
                        errorAlert();
                    }
                    System.out.println("Double click on: "+rowData.getName());
                }
            });
            return row ;
        });
    }
    private int addVoterElection(){
        Configuration config = new Configuration();
        config.configure();
        config.addAnnotatedClass(ElectionVoter.class);
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
        factory = config.buildSessionFactory(serviceRegistry);
        Session session = factory.openSession();
        Transaction tx = null;
        Integer electionVoterIdSaved = null;
        try {
            tx = session.beginTransaction();
            electionVoterIdSaved = (Integer) session.save(new ElectionVoter(election.getId(), voter.getId()));
            tx.commit();
        } catch(HibernateException ex) {
            if(tx != null)
                tx.rollback();
            ex.printStackTrace();
        } finally {
            session.close();
        }

        return electionVoterIdSaved;
    }
    private int voteCandidate(Candidate candidate) {
        Session sesn = factory.openSession();
        Transaction tx;
        int result = 0;
        try {
            tx = sesn.beginTransaction();
            result = sesn.createQuery("update Candidate" +
                    " set  votecounter= '" +  (candidate.getVotecounter()+1) + "'" +
                    " where id = '" + candidate.getId() + "'").executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            sesn.close();
        }
        return result;
    }
    private int searchForVoter(){
        FingerPrintSensor fingerPrintSensor = new FingerPrintSensor("COM3", 9600);
        fingerPrintSensor.create();
        if(!fingerPrintSensor.connect()){
            exit(1);
        }
        int retVal = fingerPrintSensor.search();
        fingerPrintSensor.disconnect();
        return retVal;
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
            candidates = (List) sesn.createQuery("from Candidate where electionid = '" + election.getElectionID() + "'").list();
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            sesn.close();
        }

        return candidates;
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
    private void voteAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Voting");
        alert.setHeaderText("You are not voting for \""+ candidate.getName() + "\"");
        alert.setContentText("Put your finger to vote !!");
        alert.showAndWait();
    }
    private void errorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Vote error");
        alert.setHeaderText("Fingerprint did not pair !!! Try again");
        //alert.setContentText("");
        alert.showAndWait();
    }
}
