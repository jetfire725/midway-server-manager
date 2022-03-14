package com.bateman.midway.view;

import com.bateman.midway.service.FileProcessor;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.File;

public class ClientView {

    public static VBox getClientLayout(){
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
        updateGameServersButton.setOnAction(event -> loadServer(serverIdBox));
        return vLayout;
    }

    private static void loadServer(TextField serverIdBox){
        PrimaryView.setStatusBar(PrimaryView.Status.THINKING);
        PrimaryView.statusLabel.setText("Checking Server ID...");
        if (serverIdBox.getText().isEmpty()){
            serverIdBox.setPromptText("Enter Server ID!");
            return;
        }
        File file = new File(FileProcessor.properties.getProperty("gameDir")+"/servers.dat");
        if (!file.exists()){
            PrimaryView.setStatusBar(PrimaryView.Status.FAIL);
            PrimaryView.statusLabel.setText("Game directory not found!");
            return;
        }

        Thread thread = new Thread(()->{
            FileProcessor.updateClientServerAddress(file.getAbsolutePath(), serverIdBox.getText());
            Platform.runLater(() -> {
                PrimaryView.setStatusBar(PrimaryView.Status.SUCCESS);
                PrimaryView.statusLabel.setText("Game servers updated");
            });
        });
        thread.start();

        }
}
