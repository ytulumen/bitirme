package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import sample.Model.Candidate;
import sample.Model.Voter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {


    @FXML
    private ChoiceBox<String> choiceBox = new ChoiceBox<>();

    @FXML
    private TextField userName = new TextField();

    @FXML
    private PasswordField passwordField = new PasswordField();

    @FXML
    private Button loginButton;

    private int candidateid;
    private int voterid;
    private static SessionFactory factory;
    private static ServiceRegistry serviceRegistry;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        choiceBox.getItems().addAll(
                "Admin",
                "Voter",
                "Candidate"
        );

        choiceBox.setValue("Voter");
        userName.setText("5555");
        passwordField.setText("5555");

    }

    public void login(ActionEvent event) throws IOException {
        String source = "";
        FXMLLoader loader = null;
        Parent root = null;
        if (choiceBox.getValue() == "Admin"
                && userName.getText().equals("Admin")
                && passwordField.getText().equals("admin")) {
            source = "AdminPanel";
            loader = new FXMLLoader(getClass().getClassLoader().getResource("fx/" + source + ".fxml"));
            root = loader.load();

        } else if (choiceBox.getValue() == "Voter") {
            Configuration config = new Configuration();
            config.configure();
            config.addAnnotatedClass(Voter.class);
            serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
            factory = config.buildSessionFactory(serviceRegistry);
            List<Voter> voters = this.getVoters();
            boolean flag = true;

            for (Voter voter : voters) {
                if (Long.toString(voter.getIdentityNumber()).equals(userName.getText())
                        && voter.getPassword().equals(passwordField.getText())) {
                    source = "VoterPanel";
                    flag = false;
                    loader = new FXMLLoader(getClass().getClassLoader().getResource("fx/" + source + ".fxml"));
                    root = loader.load();
                    VoterPanelController voterPanelController = loader.getController();
                    voterPanelController.setVoterFromOutside(voter);
                    voterPanelController.loadVoter();
                }
            }
            if (flag) {
                errorAlert();
                source = "MainPage";
                loader = new FXMLLoader(getClass().getClassLoader().getResource("fx/" + source + ".fxml"));
                root = loader.load();
            }
        } else if (choiceBox.getValue() == "Candidate") {

            Configuration config = new Configuration();
            config.configure();
            config.addAnnotatedClass(Candidate.class);
            serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
            factory = config.buildSessionFactory(serviceRegistry);
            List<Candidate> candidates = this.getCandidates();
            boolean flag = true;
            for (Candidate candidate : candidates) {
                if (Long.toString(candidate.getIdentityNumber()).equals(userName.getText())
                        && candidate.getPassword().equals(passwordField.getText())) {
                    source = "CandidatePanel";
                    flag = false;
                    loader = new FXMLLoader(getClass().getClassLoader().getResource("fx/" + source + ".fxml"));
                    root = loader.load();
                    CandidatePanelController candidatePanelController = loader.getController();
                    candidatePanelController.setCandidateIdFromOutside(candidate.getId());
                    candidatePanelController.loadCandidate();
                }
            }
            if (flag) {
                errorAlert();
                source = "MainPage";
                loader = new FXMLLoader(getClass().getClassLoader().getResource("fx/" + source + ".fxml"));
                root = loader.load();
            }
        } else {
            errorAlert();
            source = "MainPage";
            loader = new FXMLLoader(getClass().getClassLoader().getResource("fx/" + source + ".fxml"));
            root = loader.load();

        }
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        primaryStage.setTitle("Online Election System");
        primaryStage.setScene(new Scene(root, 750,700));
        primaryStage.show();


    }

    private List<Voter> getVoters() {
        Session sesn = factory.openSession();
        Transaction tx = null;
        List<Voter> voters = new ArrayList<Voter>();
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

    private List<Candidate> getCandidates() {
        Session sesn = factory.openSession();
        Transaction tx = null;
        List<Candidate> candidates = new ArrayList<Candidate>();
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
        alert.setTitle("Login Error");
        alert.setHeaderText("You are not authorized");
        alert.setContentText("Check your username and password");
        alert.showAndWait();
    }
}
