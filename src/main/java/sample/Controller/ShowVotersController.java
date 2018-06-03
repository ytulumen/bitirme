package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import sample.Model.Election;
import sample.Model.Voter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ShowVotersController implements Initializable {

    private static SessionFactory factory;
    private static ServiceRegistry serviceRegistry;
    private ActionEvent actionEvent;

    @FXML
    private TableView<Voter> voterTable;

    @FXML
    private TableColumn<Voter, Integer> idColumn;

    @FXML
    private TableColumn<Voter, String> nameColumn;

    @FXML
    private TableColumn<Voter, String> surnameColumn;

    @FXML
    private TableColumn<Voter, String> addressColumn;

    private ObservableList<Voter> observableVoter = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(new PropertyValueFactory<Voter, Integer>("identityNumber"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Voter, String>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<Voter, String>("surname"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<Voter, String>("street"));

        observableVoter.addAll(getVoters());
        voterTable.setItems(observableVoter);
    }
    public void logout(ActionEvent event) throws IOException {
        this.actionEvent = event;
        loadScene("MainPage");
    }

    @FXML
    public void back(ActionEvent event) {
        this.actionEvent = event;
        loadScene("AdminPanel");
    }

    private List<Voter> getVoters() {
        Configuration config = new Configuration();
        config.configure();
        config.addAnnotatedClass(Voter.class);
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
        factory = config.buildSessionFactory(serviceRegistry);
        Session sesn = factory.openSession();
        Transaction tx = null;
        List<Voter> voters = new ArrayList<>();
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
}
