package sample.Controller;

import javafx.embed.swing.SwingFXUtils;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
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
        boolean emptyFlag = true;
        List<Candidate> candidates = this.getCandidates();

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
        if(description.getText() == null || description.getText().trim().isEmpty()){
            emptyFlag = false;
            description.getStyleClass().remove("best");
            description.getStyleClass().add("error");
        }else {
            description.getStyleClass().add("best");
        }
        if(identityNumber.getText() == null || identityNumber.getText().trim().isEmpty()){
            emptyFlag = false;
            identityNumber.getStyleClass().remove("best");
            identityNumber.getStyleClass().add("error");
        }else {
            identityNumber.getStyleClass().add("best");
        }
        if(electionid.getText() == null || electionid.getText().trim().isEmpty()){
            emptyFlag = false;
            electionid.getStyleClass().remove("best");
            electionid.getStyleClass().add("error");
        }else {
            electionid.getStyleClass().add("best");
        }
        if(selectedFile == null || selectedFile.getAbsolutePath().trim().isEmpty()){
            emptyFlag = false;
            errorAlert("Image can not be null");
        }

        if(emptyFlag){
            try {
                Long id = Long.parseLong(identityNumber.getText(), 10);
                for (Candidate candidate: candidates ) {
                    if(candidate.getIdentityNumber() == id){
                        errorAlert("There is an candidate with " + identityNumber.getText()
                                + " id or check your database connections");
                        idControlFlag = false;
                        identityNumber.getStyleClass().remove("best");
                        identityNumber.getStyleClass().add("error");
                        break;
                    }
                }
            }catch (NumberFormatException e){
                errorAlert("Invalid candidate id " + identityNumber.getText()
                        + " or check your database connections");
                idControlFlag = false;
                identityNumber.getStyleClass().remove("best");
                identityNumber.getStyleClass().add("error");
                e.printStackTrace();
            }
            if(idControlFlag){
                List<Election> elections = this.getElections();
                try {
                    int id = Integer.parseInt(electionid.getText());
                    for (Election election: elections ) {
                        if(election.getElectionID() == id && election.isVotable()){
                            electionIdControlFlag = true;
                            break;
                        }
                    }
                }catch (NumberFormatException e){
                    electionIdControlFlag = false;
                    errorAlert("Invalid election id " + electionid.getText());
                    electionid.getStyleClass().remove("best");
                    electionid.getStyleClass().add("error");
                    e.printStackTrace();
                }
                if(!electionIdControlFlag){
                    errorAlert("There is any election with election id = " + electionid.getText());
                    electionid.getStyleClass().remove("best");
                    electionid.getStyleClass().add("error");

                }
                else{
                    insertCandidate(new Candidate(name.getText(), surname.getText(), Long.parseLong(identityNumber.getText(), 10),
                            password.getText(), street.getText(), number.getText(), town.getText(), city.getText(), description.getText(),
                            selectedFile.getAbsolutePath(), Integer.parseInt(electionid.getText()), 0));
                    this.actionEvent = event;
                    loadScene("AdminPanel");
                }
            }
        }
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
            primaryStage.setTitle("Online Election System");
            primaryStage.setScene(new Scene(root, 750,700));
            primaryStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @FXML
    private void loadImage(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString() + "/src/main/resources/Pictures/Candidates";
        fileChooser.setInitialDirectory(new File(currentPath));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        selectedFile = fileChooser.showOpenDialog(new Stage());
        try {
            BufferedImage bufferedImage;
            bufferedImage = ImageIO.read(selectedFile);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            imageView.setImage(image);
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
            elections = (List) sesn.createQuery("from Election where isVotable= '" + 1 +"'").list();
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            sesn.close();
        }

        return elections;
    }
    private void emptyAlert(String alertString) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Empty field error");
        alert.setContentText(alertString + " can not be null !");
        alert.showAndWait();
    }
}
