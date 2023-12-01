package com.example.cnpm;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Menu{
    @FXML
    private AnchorPane newMemberPane;

    @FXML
    private VBox container;

    @FXML
    private ScrollPane scrollMember;

    @FXML
    private Button newMemberBtn;

    @FXML
    private AnchorPane peoplePane;

    @FXML
    private AnchorPane homePane;

    @FXML
    void addNewMember(ActionEvent event) {
        Insets insets = new Insets(10, 20, 0, 20);
        HBox hbox = new HBox(10); // Khoảng cách giữa các phần tử trong HBox là 10
        Label cmnd = new Label("CMND:");
        TextField cmndText = new TextField();
        cmndText.setPromptText("CMND");
        Label qh = new Label("Quan hệ với chủ hộ:");
        TextField qhText = new TextField();
        qhText.setPromptText("Quan hệ với chủ hộ");
        Button button = new Button("Check");
        button.getStyleClass().add("detail-btn");

        hbox.getChildren().addAll(cmnd, cmndText, qh, qhText, button);
        hbox.setPadding(insets);
        container.getChildren().add(hbox);
    }


}
