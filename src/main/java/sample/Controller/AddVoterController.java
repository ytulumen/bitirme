package sample.Controller;

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
import sample.Model.FingerPrintSensor;
import sample.Model.Voter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static java.lang.System.exit;

// TODO: add finger print readerrrr
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
        boolean insertFlag = true, emptyFlag = true;
        List<Voter> voters = this.getVoters();

        if(name.getText() == null || name.getText().trim().isEmpty()){
            name.getStyleClass().remove("best");
            name.getStyleClass().add("error");
            emptyFlag = false;
        }else {
            name.getStyleClass().add("best");
        }
        if(surname.getText() == null || surname.getText().trim().isEmpty()){
            emptyFlag = false;
            surname.getStyleClass().remove("best");
            surname.getStyleClass().add("error");
        }else {
            surname.getStyleClass().add("best");
        }
        if(password.getText() == null || password.getText().trim().isEmpty()){
            emptyFlag = false;
            password.getStyleClass().remove("best");
            password.getStyleClass().add("error");
        }else {
            password.getStyleClass().add("best");
        }
        if(street.getText() == null || street.getText().trim().isEmpty()){
            emptyFlag = false;
            street.getStyleClass().remove("best");
            street.getStyleClass().add("error");
        }else {
            street.getStyleClass().add("best");
        }
        if(number.getText() == null || number.getText().trim().isEmpty()){
            emptyFlag = false;
            number.getStyleClass().remove("best");
            number.getStyleClass().add("error");
        }else {
            number.getStyleClass().add("best");
        }
        if(town.getText() == null || town.getText().trim().isEmpty()){
            emptyFlag = false;
            town.getStyleClass().remove("best");
            town.getStyleClass().add("error");
        }else {
            town.getStyleClass().add("best");
        }
        if(city.getText() == null || city.getText().trim().isEmpty()){
            emptyFlag = false;
            city.getStyleClass().remove("best");
            city.getStyleClass().add("error");
        }else {
            city.getStyleClass().add("best");
        }
        if(identityNumber.getText() == null || identityNumber.getText().trim().isEmpty()){
            emptyFlag = false;
            identityNumber.getStyleClass().remove("best");
            identityNumber.getStyleClass().add("error");
        }else {
            identityNumber.getStyleClass().add("best");
        }

        if(emptyFlag){
            try {
                for (Voter voter: voters ) {
                    if(voter.getIdentityNumber() == Long.parseLong(identityNumber.getText(), 10)){
                        errorAlert("There is an voter with " + identityNumber.getText()
                                + " id or check your database connection");
                        insertFlag = false;
                        identityNumber.getStyleClass().remove("best");
                        identityNumber.getStyleClass().add("error");
                        break;
                    }
                }
                if (insertFlag){
                    int fingerPrintId = 1;
                    for (int i = 1; i < 127; i++) {
                        boolean fpflag = true;
                        for (Voter voter: voters) {
                            if (voter.getFingerPrintID() == i){
                                fpflag = false;
                            }
                        }
                        if (fpflag){
                            fingerPrintId = i;
                            break;
                        }
                    }
                    infoAlert();
                    enrollVoter(fingerPrintId);
                    insertVoter(new Voter(name.getText(), surname.getText(), Long.parseLong(identityNumber.getText(), 10),
                            password.getText(), street.getText(), number.getText(), town.getText(), city.getText(), fingerPrintId));
                    successfulAlert();
                    actionEvent = event;
                    loadScene("AdminPanel");
                }
            }catch (NumberFormatException e){
                e.printStackTrace();
                errorAlert("Invalid voter id " + identityNumber.getText());
                identityNumber.getStyleClass().remove("best");
                identityNumber.getStyleClass().add("error");
            }
        }
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
            primaryStage.setScene(new Scene(root, 750,600));
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
    private void errorAlert(String errorString) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Database Error");
        alert.setContentText(errorString);
        alert.showAndWait();
    }
    private void infoAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("INFORMATION");
        alert.setHeaderText("Fingerprint Sensor");
        alert.setContentText("Please put your finger top of the fingerprint " +
                "until the light is turned off\n" +
                "Remove and put it again !");
        alert.showAndWait();
    }
    private void successfulAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONFIRMATION");
        alert.setHeaderText("Voter saved");
        alert.setContentText("Voter saved successfully !");
        alert.showAndWait();
    }
    private boolean enrollVoter(int id){
        FingerPrintSensor fingerPrintSensor = new FingerPrintSensor("COM3", 9600);
        fingerPrintSensor.create();
        if(!fingerPrintSensor.connect()){
            exit(1);
        }
        boolean retVal = fingerPrintSensor.enroll(id);
        fingerPrintSensor.disconnect();
        return retVal;
    }

}
