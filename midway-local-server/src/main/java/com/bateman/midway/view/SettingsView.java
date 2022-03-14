package com.bateman.midway.view;

import com.bateman.midway.service.FileProcessor;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class SettingsView {

    public static VBox getSettingsLayout(){
        VBox vLayout = new VBox();
        vLayout.setPadding(new Insets(10,10,10,10));

        //GAME DIRECTORY LAYOUT
        HBox gameDirLayout = new HBox();
        String username = System.getProperty("user.name");
        Label gameDirLabel = new Label("Game Directory: ");
        TextField gameDirField = new TextField();
        gameDirField.setEditable(false);
        String gameDir = "C:/Users/" + username + "/AppData/Roaming/.minecraft";
        if (new File(gameDir).exists()){
            FileProcessor.properties.setProperty("gameDir", gameDir);
            gameDirField.setText(gameDir);
        } else {
            gameDirField.setPromptText("Browse .minecraft folder");
        }
        gameDirField.setMinWidth(350);
        gameDirLayout.setPadding(new Insets(0,0,10,0));
        Button gameBrowseBtn = new Button("  ...  ");
        gameBrowseBtn.setOnAction(event -> {
            String dir =getDirectory();
            if (dir != null) {
                gameDirField.setText(dir);
                FileProcessor.properties.setProperty("gameDir", dir );
            }

        });
        gameDirLayout.getChildren().addAll(gameDirField, gameBrowseBtn);

        //SERVER DIRECTORY LAYOUT
        HBox serverDirLayout = new HBox();
        Label serverDirLabel = new Label("Server Directory:");
        TextField serverDirField = new TextField();
        serverDirField.setEditable(false);
        serverDirField.setMinWidth(350);
        serverDirField.setPromptText("Browse server directory");
        Button serverBrowseBtn = new Button("  ...  ");
        serverBrowseBtn.setOnAction(event -> {
            String dir = getDirectory();
            if (dir != null){
                serverDirField.setText(dir);
                FileProcessor.properties.setProperty("serverDir", dir);
            }

        });
        serverDirLayout.getChildren().addAll(serverDirField, serverBrowseBtn);
        vLayout.getChildren().addAll(gameDirLabel,gameDirLayout, serverDirLabel, serverDirLayout);
        return vLayout;
    }

    private static String getDirectory(){
        DirectoryChooser dirChooser = new DirectoryChooser();
        Stage stage = new Stage();
        stage.setTitle("Browse directory");
        File dir = dirChooser.showDialog(stage);
        if (dir != null){
            return dir.getAbsolutePath();
        }
        return null;
    }
}
