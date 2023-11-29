package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class Login {
    @FXML
    private Label informLoginErrorLabel;

    @FXML
    private Button loginbutton;

    @FXML
    private TextField password_textfield;

    @FXML
    private TextField username_textfield;

    @FXML
    void clickonloginAction(MouseEvent event) throws IOException {
        String username = username_textfield.getText();
        String password = password_textfield.getText();
        if(username.equals("totruong") && password.equals("123")) {
            Stage loginStage = (Stage) loginbutton.getScene().getWindow();
            loginStage.close();
            Stage stage = new Stage();
            FXMLLoader loaders = new FXMLLoader(getClass().getResource("TotruongStage.fxml"));
            Parent root = loaders.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }
        else {
            informLoginErrorLabel.setText("Tên đăng nhập hoặc mật khẩu sai");
            username_textfield.clear();
            password_textfield.clear();
        }
    }

}
