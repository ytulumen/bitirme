package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import sample.Model.Candidate;
import sample.Model.Election;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddCandidateController implements Initializable {
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
    private TextArea description;

    @FXML
    private TextField electionid;

    @FXML
    private ImageView imageView;
    @FXML
    private Button submitButton;

    private ActionEvent actionEvent;
    private static SessionFactory factory;
    private static ServiceRegistry serviceRegistry;
    private File selectedFile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void submitCandidate(ActionEvent event)   {
        boolean idControlFlag = true, electionIdControlFlag = false;
        List<Candidate> candidates = this.getCandidates();
        for (Candidate candidate: candidates ) {
            if(candidate.getIdentityNumber() == Long.parseLong(identityNumber.getText(), 10)){
                errorAlert();
                idControlFlag = false;
            }

        }
        List<Election> elections = this.getElections();
        for (Election election: elections ) {
            if(election.getElectionID() == Integer.parseInt(electionid.getText())){
                electionIdControlFlag = true;
                break;
            }
        }
        if(!electionIdControlFlag){
            errorAlert();
        }
        if (idControlFlag && electionIdControlFlag){
            insertCandidate(new Candidate(name.getText(), surname.getText(), Long.parseLong(identityNumber.getText(), 10),
                    password.getText(), street.getText(), number.getText(), town.getText(), city.getText(), description.getText(),
                     selectedFile.getAbsolutePath(), Integer.parseInt(electionid.getText()), 0));
        }

        this.actionEvent = event;
        loadScene("AdminPanel");
    }
    public void back(ActionEvent event) {
        this.actionEvent = event;
        loadScene("AdminPanel");
    }
    public void logout(ActionEvent event)  {
        this.actionEvent = event;
        loadScene("MainPage");
    }
    private int insertCandidate(Candidate candidate)
    {
        Configuration config = new Configuration();
        config.configure();
        config.addAnnotatedClass(Candidate.class);
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
        factory = config.buildSessionFactory(serviceRegistry);

        Session session = factory.openSession();
        Transaction tx = null;
        Integer userIdSaved = null;
        try {
            tx = session.beginTransaction();
            userIdSaved = (Integer) session.save(candidate);
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
    private List<Candidate> getCandidates() {
        Configuration config = new Configuration();
        config.configure();
        config.addAnnotatedClass(Candidate.class);
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
        factory = config.buildSessionFactory(serviceRegistry);

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
    @FXML
    private void loadImage(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        selectedFile = fileChooser.showOpenDialog(new Stage());
    }
    private void errorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Database Error");
        alert.setContentText("There is an candidate with same id or check your database connections");
        alert.showAndWait();
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
}
