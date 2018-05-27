package sample.Controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import sample.Model.Voter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EditVoterController implements Initializable {
    private static SessionFactory factory;
    private static ServiceRegistry serviceRegistry;
    private int voterId;
    private ActionEvent actionEvent;
    private Voter voter;

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

    private long identityNumber;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Configuration config = new Configuration();
        config.configure();
        config.addAnnotatedClass(Voter.class);
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
        factory = config.buildSessionFactory(serviceRegistry);

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

    @FXML
    public void submitVoter(ActionEvent event) {
        boolean emptyFlag = true;

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
        if (emptyFlag){
            updateVoter(new Voter(name.getText(), surname.getText(), voter.getIdentityNumber(),
                    password.getText(), street.getText(), number.getText(), town.getText(), city.getText(), voter.getFingerPrintID()));
            successfulAlert();
            actionEvent = event;
            loadScene("AdminPanel");
        }
    }

    private Voter getVoter() {
        Session sesn = factory.openSession();
        Transaction tx;
        List<Voter> voters = new ArrayList<>();
        try {
            tx = sesn.beginTransaction();
            voters = (List) sesn.createQuery("from Voter where id = '" + voterId + "'").list();
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            sesn.close();
        }
        return voters.get(0);
    }
    public void setVoterIdFromOutside(int id){
        this.voterId = id;
    }
    public void loadVoter(){
        voter = getVoter();
        identityNumber = voter.getIdentityNumber();
        name.setText(voter.getName());
        surname.setText(voter.getSurname());
        password.setText(voter.getPassword());
        street.setText(voter.getStreet());
        number.setText(voter.getNumber());
        town.setText(voter.getTown());
        city.setText(voter.getCity());
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

    private int updateVoter(Voter voter) {
        Session sesn = factory.openSession();
        Transaction tx;
        int result = 0;
        try {
            tx = sesn.beginTransaction();
            result = sesn.createQuery("update Voter" +
                    " set fname = '" +  voter.getName() + "'" +
                    ", surname = '" + voter.getSurname() + "'" +
                    ", pword = '" + voter.getPassword() + "'" +
                    ", street = '" + voter.getStreet() + "'" +
                    ", dnumber = '" + voter.getNumber() + "'" +
                    ", town = '" + voter.getTown() + "'" +
                    ", city = '" + voter.getCity() + "'" +
                    " where id = '" + voterId + "'").executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            sesn.close();
        }
        return result;
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
        alert.setContentText("Voter edited successfully");
        alert.showAndWait();
    }
}
