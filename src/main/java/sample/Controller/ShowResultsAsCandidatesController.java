package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
import sample.Model.Candidate;
import sample.Model.Election;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ShowResultsAsCandidatesController implements Initializable {
    private static SessionFactory factory;
    private static ServiceRegistry serviceRegistry;
    private ActionEvent actionEvent;
    private Election election;

    @FXML
    private TableView<Candidate> candidateTable;

    @FXML
    private TableColumn<Candidate, Integer> idColumn;

    @FXML
    private TableColumn<Candidate, String> nameColumn;

    @FXML
    private TableColumn<Candidate, String> surnameColumn;

    @FXML
    private TableColumn<Candidate, String> voteColumn;

    @FXML
    private TableColumn<Candidate, String> descriptionColumn;

    @FXML
    private TableColumn<Candidate, Integer> electionIdColumn;

    @FXML
    private TableColumn<Candidate, ImageView> imageColumn;

    private ObservableList<Candidate> observableCandidate = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(new PropertyValueFactory<Candidate, Integer>("identityNumber"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Candidate, String>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<Candidate, String>("surname"));
        voteColumn.setCellValueFactory(new PropertyValueFactory<Candidate, String>("votecounter"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Candidate, String>("description"));
        electionIdColumn.setCellValueFactory(new PropertyValueFactory<Candidate, Integer>("electionid"));


        imageColumn.setCellValueFactory(new PropertyValueFactory<Candidate, ImageView>("imageView"));
        imageColumn.setMinWidth(100);
        /*imageColumn.setCellValueFactory(new Callback<TableColumn<Candidate,Image>,TableCell<Candidate,Image>>(){
            @Override
            public TableCell<Candidate,Image> call(TableColumn<Candidate,Image> param) {
                TableCell<Candidate,Image> cell = new TableCell<Candidate,Image>(){
                    public void updateItem(Candidate item, boolean empty) {
                        if(item!=null){
                            ImageView imageview = new ImageView();
                            imageview.setFitHeight(50);
                            imageview.setFitWidth(50);
                            imageview.setImage(new Image());
                        }
                    }
                };
                return cell;
            }

        });*/
    }
    @FXML
    public void logout(ActionEvent event) throws IOException {
        this.actionEvent = event;
        loadScene("MainPage");
    }
    @FXML
    public void back(ActionEvent event) {
        this.actionEvent = event;
        loadScene("AdminPanel");
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
        for (Candidate candidate:candidates ) {
            candidate.setImage();
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
    public void setVariables(Election election){
        this.election = election;
        observableCandidate.addAll(getCandidates());
        candidateTable.setItems(observableCandidate);
    }
}
