package com.bateman.midway.view;

import com.bateman.midway.service.FileProcessor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class PrimaryView extends Application {
    public static Label statusLabel = new Label();
    public static ProgressBar statusBar = new ProgressBar(0);

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Midway Manager");
        Image image = new Image(getClass().getResource("/icon.png").toExternalForm());
        primaryStage.getIcons().add(image);
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Tab clientTab = new Tab("Client"  , getClientLayout());
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



    private VBox getClientLayout(){
        VBox vLayout = new VBox();
        vLayout.setPadding(new Insets(10,10,10,10));

        //LOAD SERVER LAYOUT
        Button updateGameServersButton = new Button("Load Server");
        TextField serverIdBox = new TextField();
        Font font = Font.font("Verdana", FontWeight.BOLD,16);
        serverIdBox.setPromptText("Server ID");
        serverIdBox.setFont(font);
        HBox loadServerLayout = new HBox();
        loadServerLayout.getChildren().addAll(serverIdBox, updateGameServersButton);
        loadServerLayout.setPadding(new Insets(0, 0, 10, 0));
        vLayout.getChildren().addAll(loadServerLayout);

        //ACTIONS
        updateGameServersButton.setOnAction(event -> {
           loadServer(serverIdBox);

        });
        return vLayout;
    }

    private void loadServer(TextField serverIdBox){
        statusBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        statusLabel.setText("Checking Server ID...");
        Thread thread = new Thread(){
            public void run(){
                if (!serverIdBox.getText().isEmpty() ){
                    String result = FileProcessor.updateClientServerAddress(FileProcessor.properties.getProperty("gameDir")+"servers.dat", serverIdBox.getText());
                } else {
                    serverIdBox.setPromptText("Enter Server ID!");
                }
                Platform.runLater(()->{
                    statusBar.setProgress(0);
                    statusLabel.setText("Game Servers Updated");
                });
            }
        };
        thread.start();

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







}
