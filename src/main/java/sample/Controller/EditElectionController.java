package sample.Controller;

import arduino.Arduino;
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
import sample.Model.Election;
import sample.Model.Election;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EditElectionController implements Initializable {
    private static SessionFactory factory;
    private static ServiceRegistry serviceRegistry;
    private ActionEvent actionEvent;
    private Election election;
    private int electionid;

    @FXML
    private TextArea topic;

    @FXML
    private TextField title;

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
        boolean emptyFlag = true;

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

        if (emptyFlag){
            updateElection(new Election(election.getId(), topic.getText(), title.getText(), election.isVotable()));
            successfulAlert();
            actionEvent = event;
            loadScene("AdminPanel");
        }
    }
    private int updateElection(Election election) {
        Session sesn = factory.openSession();
        Transaction tx;
        int result = 0;
        try {
            tx = sesn.beginTransaction();
            result = sesn.createQuery("update Election" +
                    " set topic = '" +  election.getTopic() + "'" +
                    ", title = '" + election.getTitle() + "'" +
                    " where id = '" + electionid + "'").executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            sesn.close();
        }
        return result;
    }
    public void setElectionIdFromOutside(int id){
        this.electionid = id;
    }
    public void loadElection(){
        election = getElection();
        title.setText(election.getTitle());
        topic.setText(election.getTopic());
    }
    private Election getElection() {
        Session sesn = factory.openSession();
        Transaction tx;
        List<Election> elections = new ArrayList<>();
        try {
            tx = sesn.beginTransaction();
            elections = (List) sesn.createQuery("from Election where id = '" + electionid + "'").list();
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            sesn.close();
        }
        return elections.get(0);
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
        alert.setHeaderText("Edited");
        alert.setContentText("Election edited successfully");
        alert.showAndWait();
    }
}
