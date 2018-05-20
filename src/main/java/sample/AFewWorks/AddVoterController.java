package sample.AFewWorks;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
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

public class AddVoterController implements Initializable {
    @FXML
    private TextField identityNumber;

    @FXML
    private TextField name;

    @FXML
    private TextField surname;

    @FXML
    private PasswordField password;

    @FXML
    private TextField street;

    @FXML
    private TextField number;

    @FXML
    private TextField town;

    @FXML
    private TextField city;

    @FXML
    private Button submitButton;

    private ActionEvent actionEvent;

    private static SessionFactory factory;
    private static ServiceRegistry serviceRegistry;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.submitButton.setDisable(false);

        Configuration config = new Configuration();
        config.configure();
        config.addAnnotatedClass(Voter.class);
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
        factory = config.buildSessionFactory(serviceRegistry);
    }

    public void submitVoter(ActionEvent event) {
        boolean insertFlag = true;
        List<Voter> voters = this.getVoters();
        for (Voter voter: voters ) {
            if(voter.getIdentityNumber() == Long.parseLong(identityNumber.getText(), 10)){
                errorAlert();
                insertFlag = false;
            }
        }
        if (insertFlag){
            insertVoter(new Voter(name.getText(), surname.getText(), Long.parseLong(identityNumber.getText(), 10),
                    password.getText(), street.getText(), number.getText(), town.getText(), city.getText()));
        }
        actionEvent = event;
        loadScene("AdminPanel");

    }
    public void back(ActionEvent event) {
        actionEvent = event;
        loadScene("AdminPanel");
    }
    public void logout(ActionEvent event){
        actionEvent = event;
        loadScene("MainPanel");
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
    private int insertVoter(Voter voter)
    {
        Session session = factory.openSession();
        Transaction tx = null;
        Integer userIdSaved = null;
        try {
            tx = session.beginTransaction();
            userIdSaved = (Integer) session.save(voter);
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
    private void errorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Database Error");
        alert.setContentText("There is an election with same id or check your database connections");
        alert.showAndWait();
    }
}
