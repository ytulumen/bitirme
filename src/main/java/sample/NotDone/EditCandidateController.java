package sample.NotDone;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import sample.Model.Candidate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EditCandidateController implements Initializable {
    private static SessionFactory factory;
    private static ServiceRegistry serviceRegistry;
    private int candidateId;
    private Candidate candidate;
    private ActionEvent actionEvent;

    private long identityNumber;  //because id not changeable

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
    private ImageView imageView;

    private File selectedFile;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Configuration config = new Configuration();
        config.configure();
        config.addAnnotatedClass(Candidate.class);
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
        factory = config.buildSessionFactory(serviceRegistry);

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
    public void submitCandidate(ActionEvent event) {
        if(name.getText() == null || name.getText().trim().isEmpty()){
            name.setText(candidate.getName());
        }
        if(surname.getText() == null || surname.getText().trim().isEmpty()){
            surname.setText(candidate.getSurname());
        }
        if(password.getText() == null || password.getText().trim().isEmpty()){
            password.setText(candidate.getPassword());
        }
        if(street.getText() == null || street.getText().trim().isEmpty()){
            street.setText(candidate.getStreet());
        }
        if(number.getText() == null || number.getText().trim().isEmpty()){
            number.setText(candidate.getNumber());
        }
        if(town.getText() == null || town.getText().trim().isEmpty()){
            town.setText(candidate.getTown());
        }
        if(city.getText() == null || city.getText().trim().isEmpty()){
            city.setText(candidate.getCity());
        }
        if(description.getText() == null || description.getText().trim().isEmpty()){
            description.setText(candidate.getDescription());
        }
        if(selectedFile == null || selectedFile.getAbsolutePath().trim().isEmpty()){
            selectedFile = new File(candidate.getImagePath());
        }


        updateCandidate(new Candidate(name.getText(), surname.getText(), identityNumber,
                password.getText(), street.getText(), number.getText(), town.getText(), city.getText(), description.getText(),
                selectedFile.getAbsolutePath(), candidate.getElectionid(), candidate.getVotecounter()));
    }

    private Candidate getCandidate() {
        Session sesn = factory.openSession();
        Transaction tx;
        List<Candidate> candidates = new ArrayList<>();
        try {
            tx = sesn.beginTransaction();
            candidates = (List) sesn.createQuery("from Candidate where identityNumber = '" + candidateId + "'").list();
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            sesn.close();
        }
        return candidates.get(0);
    }
    public void setCandidateIdFromOutside(int id){
        this.candidateId = id;
    }
    public void loadCandidate(){
        candidate = getCandidate();
        identityNumber = candidate.getIdentityNumber();
        name.setText(candidate.getName());
        surname.setText(candidate.getSurname());
        password.setText(candidate.getPassword());
        street.setText(candidate.getStreet());
        number.setText(candidate.getNumber());
        town.setText(candidate.getTown());
        city.setText(candidate.getCity());
        description.setText(candidate.getDescription());

        try {
            BufferedImage bufferedImage;
            bufferedImage = ImageIO.read(new File(candidate.getImagePath()));
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            imageView.setImage(image);

            //imageView = new ImageView(new Image(new FileInputStream( candidate.getImagePath())));

        }catch (IOException e){
            e.printStackTrace();
        }
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

    private int updateCandidate(Candidate candidate) {
        Session sesn = factory.openSession();
        Transaction tx;
        int result = 0;
        try {
            tx = sesn.beginTransaction();
            result = sesn.createQuery("update Candidate" +
                    " set fname = '" +  candidate.getName() + "'" +
                    ", surname = '" + candidate.getSurname() + "'" +
                    ", pword = '" + candidate.getPassword() + "'" +
                    ", street = '" + candidate.getStreet() + "'" +
                    ", dnumber = '" + candidate.getNumber() + "'" +
                    ", town = '" + candidate.getTown() + "'" +
                    ", city = '" + candidate.getCity() + "'" +
                    ", description = '" + candidate.getDescription() + "'" +
                    ", imagePath = '" + getPath(candidate.getImagePath()) + "'" +
                    " where identityNumber = '" + candidateId + "'").executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            sesn.close();
        }
        return result;
    }
    private String getPath(String path){
        String ret = path.replace(new StringBuilder("\\"), new StringBuilder("\\\\"));
        return ret;
    }
}
