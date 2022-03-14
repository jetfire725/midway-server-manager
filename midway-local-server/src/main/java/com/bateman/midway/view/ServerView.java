package com.bateman.midway.view;

import com.bateman.midway.service.FileProcessor;
import com.bateman.midway.service.IPService;
import com.bateman.midway.service.MojangServerClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class ServerView {

    static VBox getServerLayout(){
        //REGISTER SERVER LAYOUT
        Button updateIpButton = new Button("Register Server");
        TextField serverIdBox = new TextField();
        Font font = Font.font("Verdana", FontWeight.BOLD,16);
        serverIdBox.setPromptText("Server ID");
        serverIdBox.setFont(font);
        updateIpButton.setOnAction(event -> registerServer(serverIdBox));
        HBox registerServerLayout = new HBox();
        registerServerLayout.getChildren().addAll(serverIdBox, updateIpButton);
        registerServerLayout.setPadding(new Insets(0, 0, 10, 0));


        //DOWNLOAD JAR LAYOUT
        HBox downloadJarsLayout = new HBox();
        Label downloadJarsLabel = new Label("Download Jar:  ");
        ArrayList<String> versions = new ArrayList<>(MojangServerClient.versions.keySet());
        Collections.sort(versions);
        ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableList(versions));
        comboBox.setPromptText("Select Version");
        Button downloadButton = new Button("Download");
        downloadButton.setOnAction(event -> downloadJar(comboBox));
        downloadJarsLayout.setPadding(new Insets(0,0,10,0));
        downloadJarsLayout.getChildren().addAll(downloadJarsLabel, comboBox, downloadButton);


        VBox vlayout = new VBox();
        vlayout.setPadding(new Insets(10,10,10,10));
        vlayout.getChildren().addAll(registerServerLayout, downloadJarsLayout);



        return vlayout;
    }

    private static void registerServer(TextField serverIdBox){
        if (!serverIdBox.getText().isEmpty()){
            PrimaryView.setStatusBar(PrimaryView.Status.THINKING);
            PrimaryView.statusLabel.setText("Registering server...");
            Thread thread = new Thread(() -> {
                boolean success = IPService.updateExternalIp(serverIdBox.getText());
                Platform.runLater(()->{
                    if (success){
                        PrimaryView.setStatusBar(PrimaryView.Status.SUCCESS);
                        PrimaryView.statusLabel.setText("Server registered successfully");
                    } else {
                        PrimaryView.setStatusBar(PrimaryView.Status.FAIL);
                        PrimaryView.statusLabel.setText("Unable to register server");
                    }
                });

            });
            thread.start();

        } else {
            serverIdBox.setPromptText("Enter Server ID!");
        }
    }

    private static void downloadJar(ComboBox<String> comboBox){
        if (comboBox.getSelectionModel().isEmpty()){
            return;
        }
        String versionId = comboBox.getValue();
        File file = openSaveWindow("minecraft_server." + versionId + ".jar");
        if (file != null){
            PrimaryView.setStatusBar(PrimaryView.Status.THINKING);
            PrimaryView.statusLabel.setText("Downloading jar...");
            Thread thread = new Thread(() -> {
                String url = MojangServerClient.getSpecificVersionUrl(versionId);
                boolean success = FileProcessor.downloadServerJar(file, url);
                Platform.runLater(()->{
                    if (success){
                        PrimaryView.setStatusBar(PrimaryView.Status.SUCCESS);
                        PrimaryView.statusLabel.setText("Download complete");
                    } else {
                        PrimaryView.setStatusBar(PrimaryView.Status.FAIL);
                        PrimaryView.statusLabel.setText("Error downloading jar");
                    }


                });


            });
            thread.start();
        }
    }

    private static File openSaveWindow(String filename){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(filename);
        Stage stage = new Stage();
        stage.setTitle("Browse directory");
        return fileChooser.showSaveDialog(stage);
    }
}
