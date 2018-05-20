package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.stage.Modality;
import javafx.stage.*;

public class TextLink extends Application {

    @Override
    public void start(final Stage primaryStage) throws Exception {
        Stage     accountCreation = buildAccountCreationStage(primaryStage);
        Hyperlink createAccount   = buildCreateAccountLink(primaryStage, accountCreation);

        TextFlow flow = new TextFlow(
                new Text("Don't have an account? "), createAccount
        );
        flow.setPadding(new Insets(10));

        primaryStage.setScene(new Scene(new Group(flow)));
        primaryStage.show();
    }

    private Hyperlink buildCreateAccountLink(Stage primaryStage, Stage accountCreation) {
        Hyperlink createAccount = new Hyperlink("Click here");

        createAccount.setOnAction(event -> {
            accountCreation.setX(primaryStage.getX());
            accountCreation.setY(primaryStage.getY() + primaryStage.getHeight());
            accountCreation.show();
        });

        return createAccount;
    }

    private Stage buildAccountCreationStage(Stage primaryStage) {
        Stage accountCreation = new Stage(StageStyle.UTILITY);

        accountCreation.initModality(Modality.WINDOW_MODAL);
        accountCreation.initOwner(primaryStage);
        accountCreation.setTitle("Create Account");
        accountCreation.setScene(new Scene(new Label("<Account Creation Form Goes Here>"), 250, 50));

        return accountCreation;
    }

    public static void main(String[] args) { launch(args); }
}
