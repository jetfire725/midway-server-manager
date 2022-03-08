package com.bateman.midway.view;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ViewController extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Midway Manager");
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Tab clientTab = new Tab("Client"  , getClientLayout());
        Tab serverTab = new Tab("Server", getServerLayout());
        tabPane.getTabs().addAll(clientTab, serverTab);
        Scene scene = new Scene(tabPane);
        primaryStage.setScene(scene);
        primaryStage.show();
        tabPane.requestFocus();

    }

    private VBox getServerLayout(){
        Button portForwardButton = new Button("Execute Port Forward");
        Button updateIpButton = new Button("Update External IP");

        VBox layout = new VBox();
        layout.setPadding(new Insets(10,10,10,10));
        layout.getChildren().addAll(portForwardButton, updateIpButton);
        return layout;
    }

    private VBox getClientLayout(){
        Button updateGameServersButton = new Button("Load Server");
        TextField serverIdBox = new TextField();
        Font font = Font.font("Verdana", FontWeight.BOLD,16);
        serverIdBox.setPromptText("Server ID");
        serverIdBox.setFont(font);
        VBox vLayout = new VBox();
        vLayout.setPadding(new Insets(10,10,10,10));
        HBox hLayout = new HBox();
        hLayout.getChildren().addAll(serverIdBox, updateGameServersButton);
        vLayout.getChildren().add(hLayout);
        return vLayout;
    }
}
