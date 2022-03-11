package com.bateman.midway.view;

import com.bateman.midway.service.FileProcessor;
import com.bateman.midway.service.IPService;
import com.sun.javafx.application.HostServicesDelegate;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

public class ViewController extends Application {
    Properties properties;

    @Override
    public void start(Stage primaryStage) throws Exception {
        properties = new Properties();
        primaryStage.setTitle("Midway Manager");
        Image image = new Image(getClass().getResource("/icon.png").toExternalForm());
        primaryStage.getIcons().add(image);
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Tab clientTab = new Tab("Client"  , getClientLayout());
        Tab serverTab = new Tab("Server", getServerLayout());
        Tab settingsTab = new Tab("Settings", getSettingsLayout());
        Tab aboutTab = new Tab("About", getAboutLayout());
        tabPane.getTabs().addAll(clientTab, serverTab, settingsTab, aboutTab);
        Scene scene = new Scene(tabPane);
        primaryStage.setScene(scene);
        primaryStage.show();
        tabPane.requestFocus();

    }

    private VBox getServerLayout(){
        Button portForwardButton = new Button("Execute Port Forward");
        Button updateIpButton = new Button("Register Server");
        TextField serverIdBox = new TextField();
        Font font = Font.font("Verdana", FontWeight.BOLD,16);
        serverIdBox.setPromptText("Server ID");
        serverIdBox.setFont(font);
        HBox hLayout = new HBox();
        hLayout.getChildren().addAll(serverIdBox, updateIpButton);
        hLayout.setPadding(new Insets(0, 0, 10, 0));
        VBox vlayout = new VBox();
        vlayout.setPadding(new Insets(10,10,10,10));
        vlayout.getChildren().addAll(hLayout, portForwardButton);

        //actions
        updateIpButton.setOnAction(event -> {
            if (!serverIdBox.getText().isEmpty()){
                IPService.updateExternalIp(serverIdBox.getText());
            } else {
                serverIdBox.setPromptText("Enter Server ID!");
            }

        });

        return vlayout;
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
        Label statusLabel = new Label();
        vLayout.getChildren().addAll(loadServerLayout, statusLabel);

        //ACTIONS
        updateGameServersButton.setOnAction(event -> {
            if (!serverIdBox.getText().isEmpty() ){
                statusLabel.setText("Checking...");
                String result = FileProcessor.updateClientServerAddress(properties.getProperty("gameDir")+"servers.dat", serverIdBox.getText());
                statusLabel.setText(result);
            } else {
                serverIdBox.setPromptText("Enter Server ID!");
            }

        });
        return vLayout;
    }

    private VBox getSettingsLayout(){
        VBox vLayout = new VBox();
        vLayout.setPadding(new Insets(10,10,10,10));

        //GAME DIRECTORY LAYOUT
        HBox gameDirLayout = new HBox();
        String username = System.getProperty("user.name");
        Label gameDirLabel = new Label("Game Directory: ");
        TextField gameDirField = new TextField();
        String gameDir = "C:/Users/" + username + "/AppData/Roaming/.minecraft/";
        if (new File(gameDir).exists()){
            properties.setProperty("gameDir", gameDir);
            gameDirField.setText(gameDir);
        } else {
            gameDirField.setPromptText("Browse .minecraft folder");
        }
        gameDirField.setMinWidth(350);
        gameDirLayout.setPadding(new Insets(0,0,10,0));
        Button gameBrowseBtn = new Button("  ...  ");
        gameDirLayout.getChildren().addAll(gameDirField, gameBrowseBtn);

        //SERVER DIRECTORY LAYOUT
        HBox serverDirLayout = new HBox();
        Label serverDirLabel = new Label("Server Directory:");
        TextField serverDirField = new TextField();
        serverDirField.setMinWidth(350);
        serverDirField.setPromptText("Browse server directory");
        Button serverBrowseBtn = new Button("  ...  ");
        serverDirLayout.getChildren().addAll(serverDirField, serverBrowseBtn);
        vLayout.getChildren().addAll(gameDirLabel,gameDirLayout, serverDirLabel, serverDirLayout);
        return vLayout;
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

}