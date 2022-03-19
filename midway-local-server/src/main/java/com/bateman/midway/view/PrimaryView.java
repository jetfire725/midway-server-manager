package com.bateman.midway.view;

import com.bateman.midway.service.IPService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class PrimaryView extends Application {
    public static Label statusLabel = new Label();
    private static ProgressBar statusBar = new ProgressBar(0);
    public enum Status {
        SUCCESS,
        FAIL,
        THINKING
    }

    @Override
    public void start(Stage primaryStage) {

        //OPEN TCP PORT ON STARTUP
        startUpNPThread();

        primaryStage.setTitle("Midway Manager");
        Image image = new Image(getClass().getResource("/icon.png").toExternalForm());
        primaryStage.getIcons().add(image);
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Tab clientTab = new Tab("Client"  , ClientView.getClientLayout());
        Tab serverTab = new Tab("Server", ServerView.getServerLayout());
        Tab settingsTab = new Tab("Settings", SettingsView.getSettingsLayout());
        Tab experimentalTab = new Tab("Experimental", getExperimentalLayout());
        Tab aboutTab = new Tab("About", getAboutLayout());
        tabPane.getTabs().addAll(clientTab, serverTab, settingsTab, experimentalTab, aboutTab);
        VBox vLayout = new VBox();
        vLayout.getChildren().addAll(tabPane,statusLabel, statusBar);
        Scene scene = new Scene(vLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
        statusBar.setPrefWidth(primaryStage.getScene().getWidth());
        statusLabel.setPadding(new Insets(0,0,0,10));
        tabPane.requestFocus();
    }

    private VBox getAboutLayout(){
        VBox vLayout = new VBox();
        vLayout.setPadding(new Insets(10,10,10,10));
        Label aboutVersion = new Label("Midway-Server-Manager version 1.0 Alpha");
        Label aboutAuthor = new Label("Author: Ethan Bateman");
        Hyperlink aboutGithub = new Hyperlink("Github");
        aboutGithub.setPadding(new Insets(0,0,0,0));
        aboutGithub.setOnAction(event -> {
            try {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)){
                    Desktop.getDesktop().browse(new URI("https://github.com/jetfire725/midway-server-manager"));
                }
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });
        vLayout.getChildren().addAll(aboutVersion, aboutAuthor, aboutGithub);
        return vLayout;
    }

    private VBox getExperimentalLayout(){
        VBox vLayout = new VBox();
        vLayout.setPadding(new Insets(10,10,10,10));
        Button portForwardButton = new Button("SSH Port Forward");
        vLayout.getChildren().add(portForwardButton);
        return vLayout;
    }

    private void startUpNPThread(){
        //OPEN TCP PORT ON STARTUP
        setStatusBar(Status.THINKING);
        PrimaryView.statusLabel.setText("Attempting to enable UPnP...");
        Thread upnpThread = new Thread(()->{
            boolean uPnP = IPService.enableUPNP();
            Platform.runLater(()->{
                if (uPnP){
                    PrimaryView.statusLabel.setText("UPnP enabled");
                    setStatusBar(Status.SUCCESS);
                } else {
                    PrimaryView.statusLabel.setText("UPnP unsuccessful, check router settings");
                    setStatusBar(Status.FAIL);
                }
            });
        });
        upnpThread.start();
    }

    public static void setStatusBar(Status status){
        switch (status){
            case SUCCESS:
                statusBar.setStyle("-fx-accent: forestgreen");
                statusBar.setProgress(100);
                break;
            case FAIL:
                statusBar.setStyle("-fx-accent: firebrick");
                statusBar.setProgress(100);
                break;
            case THINKING:
                statusBar.setStyle("-fx-accent: #0093ff");
                statusBar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
                break;


        }
    }
}
