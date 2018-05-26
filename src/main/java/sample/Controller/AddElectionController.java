package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
import sample.Model.Voter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddElectionController implements Initializable {

    @FXML
    private TextField electionid;

    @FXML
    private TextArea topic;

    @FXML
    private TextField title;

    private ActionEvent actionEvent;
    private static SessionFactory factory;
    private static ServiceRegistry serviceRegistry;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Configuration config = new Configuration();
        config.configure();
        config.addAnnotatedClass(Election.class);
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
        factory = config.buildSessionFactory(serviceRegistry);
    }
    @FXML
    public void submitElection(ActionEvent event){
        boolean insertFlag = true, emptyFlag = true;
        List<Election> elections = this.getElections();

        if(topic.getText() == null || topic.getText().trim().isEmpty()){
            emptyFlag = false;
            topic.getStyleClass().remove("best");
            topic.getStyleClass().add("error");
        }else {
            topic.getStyleClass().add("best");
        }
        if(title.getText() == null || title.getText().trim().isEmpty()){
            emptyFlag = false;
            title.getStyleClass().remove("best");
            title.getStyleClass().add("error");
        }else {
            title.getStyleClass().add("best");
        }
        if(electionid.getText() == null || electionid.getText().trim().isEmpty()) {
            emptyFlag = false;
            electionid.getStyleClass().remove("best");
            electionid.getStyleClass().add("error");
        }else {
            electionid.getStyleClass().add("best");
        }

        if (emptyFlag){
            try {
                for (Election election: elections ) {
                    if(election.getElectionID() == Integer.parseInt(electionid.getText())){
                        errorAlert("There is an election with " + electionid.getText());
                        insertFlag = false;
                        electionid.getStyleClass().remove("best");
                        electionid.getStyleClass().add("error");
                        break;
                    }
                }
                if (insertFlag){
                    insertElection(new Election(Integer.parseInt(electionid.getText()), topic.getText(), title.getText(), true));
                    this.actionEvent = event;
                    loadScene("AdminPanel");
                }
            }catch (NumberFormatException e){
                e.printStackTrace();
                errorAlert("Invalid election id " + electionid.getText()
                            + "or check your database connections");
                electionid.getStyleClass().remove("best");
                electionid.getStyleClass().add("error");
            }
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
    private int insertElection(Election election)
    {
        Session session = factory.openSession();
        Transaction tx = null;
        Integer userIdSaved = null;
        try {
            tx = session.beginTransaction();
            userIdSaved = (Integer) session.save(election);
            tx.commit();
        } catch(HibernateException ex) {
            if(tx != null)
                tx.rollback();
            ex.printStackTrace();
        } finally {
            session.close();
        }

        return userIdSaved;

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
    private void errorAlert(String errorString) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Database Error");
        alert.setContentText(errorString);
        alert.showAndWait();
    }
}
