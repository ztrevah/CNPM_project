package com.example.cnpm;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class test extends Application {

    private VBox newMemberPane;

    @Override
    public void start(Stage primaryStage) throws Exception {
        newMemberPane = new VBox(); // VBox chứa thành viên mới

        Button addNewButton = new Button("Add New");
        addNewButton.setOnAction(event -> addNewMember());

        ScrollPane scrollPane = new ScrollPane(newMemberPane);
        scrollPane.setFitToWidth(true); // Tự động điều chỉnh chiều rộng theo cửa sổ
        scrollPane.setPrefViewportHeight(200); // Chiều cao ưu tiên của khu vực xem

        AnchorPane root = new AnchorPane();
        AnchorPane.setTopAnchor(addNewButton, 10.0);
        AnchorPane.setLeftAnchor(addNewButton, 10.0);
        AnchorPane.setRightAnchor(addNewButton, 10.0);
        AnchorPane.setTopAnchor(scrollPane, 40.0);
        AnchorPane.setLeftAnchor(scrollPane, 10.0);
        AnchorPane.setRightAnchor(scrollPane, 10.0);
        AnchorPane.setBottomAnchor(scrollPane, 10.0);
        root.getChildren().addAll(addNewButton, scrollPane);

        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Add New Member Example");
        primaryStage.show();
    }

    private void addNewMember() {
        Label label = new Label("Member Name:");
        TextField textField = new TextField();
        Button checkButton = new Button("Check");

        VBox memberBox = new VBox(label, textField, checkButton);

        ScrollPane memberScrollPane = new ScrollPane(memberBox);
        memberScrollPane.setFitToWidth(true); // Tự động điều chỉnh chiều rộng theo cửa sổ
        memberScrollPane.setPrefViewportHeight(80); // Chiều cao ưu tiên của khu vực xem

        newMemberPane.getChildren().add(memberScrollPane);
    }

    public static void main(String[] args) {
        launch(args);
    }
}