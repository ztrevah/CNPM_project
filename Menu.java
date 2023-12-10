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
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Menu{
    @FXML
    private TableView<people> moneyTable;

    @FXML
    private TableColumn<people, ?> missingCol;

    @FXML
    private RadioButton feeRButton;

    @FXML
    private RadioButton donateRButton;

    @FXML
    private AnchorPane newMemberPane;

    @FXML
    private Button minimize;

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
    private AnchorPane moneyPane;

    @FXML
    private Button  openPeoplePane;

    @FXML
    private Button  openHomePane;

    @FXML
    private Button  openMoneyPane;
    private double x = 0;
    private double y = 0;

    private List<Stage> newStages = new ArrayList(); //Lưu trữ tham chiếu các stage mới được mở thêm để sau logout đóng hết đi
    @FXML
    void clickClose(ActionEvent event) {//nút close trên màn hình chính, nhấn để tắt TOÀN BỘ
        System.exit(0);
    }

    @FXML
    void clickMinimize(ActionEvent event) { //nút minimize trên màn hình chính, nhấn để ẩn màn hình chính
        Stage stage = (Stage) minimize.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void clickLogout(ActionEvent e) throws IOException {//nút logout trên màn hình chính, nhấn để tắt toàn bộ cửa sổ đang mở, xoá hết dữ liệu hiện tại và mở lại stage login
        for(Stage stage : newStages){//xoá hết các stage mở thêm nhưng chưa đóng trước đó
            stage.close();
        }
        newStages.clear();

        Stage currentStage = (Stage) ((Button) e.getSource()).getScene().getWindow();
        currentStage.close();

        // Mở cửa sổ đăng nhập (hoặc cửa sổ khác tương ứng)
        Stage loginStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent loginRoot = loader.load();
        Scene loginScene = new Scene(loginRoot);
        loginRoot.setOnMousePressed((MouseEvent event) -> {
            x = event.getScreenX() - loginStage.getX();
            y = event.getScreenY() - loginStage.getY();
        });

        loginRoot.setOnMouseDragged((MouseEvent event) -> {
            loginStage.setX(event.getScreenX() - x);
            loginStage.setY(event.getScreenY() - y);
        });

        loginStage.initStyle(StageStyle.TRANSPARENT);

        loginStage.setScene(loginScene);
        loginStage.show();
    }

    /*
        Quản lý nhân khẩu
     */
    @FXML
    void openPeoplePane(ActionEvent event){//Nhấn quản lý nhân khẩu để mở peoplePane
        peoplePane.setVisible(true);
        homePane.setVisible(false);
        moneyPane.setVisible(false);
    }

    @FXML
    void clickAddPeople(ActionEvent e) { //Nhấn add trong scene quản lý nhân khẩu để mở stage mới là addPeople.fxml
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addPeople.fxml"));
            Parent addPeopleRoot = loader.load();

            // Tạo một Stage mới
            Stage addPeopleStage = new Stage();
            addPeopleStage.setScene(new Scene(addPeopleRoot));
            newStages.add(addPeopleStage);

            //Cài đặt để có thể di chuyển stage bằng kéo thả
            addPeopleRoot.setOnMousePressed((MouseEvent event) -> {
                x = event.getScreenX() - addPeopleStage.getX();
                y = event.getScreenY() - addPeopleStage.getY();
            });

            addPeopleRoot.setOnMouseDragged((MouseEvent event) -> {
                addPeopleStage.setX(event.getScreenX() - x);
                addPeopleStage.setY(event.getScreenY() - y);
            });

            // Đặt kiểu modality của Stage mới là NONE
            addPeopleStage.initModality(Modality.NONE);
            addPeopleStage.initStyle(StageStyle.TRANSPARENT);

            // Hiển thị Stage mới
            addPeopleStage.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @FXML
    void clickDetailPeople(ActionEvent e) { //Nhấn detail trong scene quản lý nhân khẩu để mở stage mới là updatePeople.fxml
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("updatePeople.fxml"));
            Parent updatePeopleRoot = loader.load();

            // Tạo một Stage mới
            Stage updatePeopleStage = new Stage();
            updatePeopleStage.setScene(new Scene(updatePeopleRoot));
            newStages.add(updatePeopleStage);

            //Cài đặt để có thể di chuyển stage bằng kéo thả
            updatePeopleRoot.setOnMousePressed((MouseEvent event) -> {
                x = event.getScreenX() - updatePeopleStage.getX();
                y = event.getScreenY() - updatePeopleStage.getY();
            });

            updatePeopleRoot.setOnMouseDragged((MouseEvent event) -> {
                updatePeopleStage.setX(event.getScreenX() - x);
                updatePeopleStage.setY(event.getScreenY() - y);
            });

            // Đặt kiểu modality của Stage mới là NONE
            updatePeopleStage.initModality(Modality.NONE);
            updatePeopleStage.initStyle(StageStyle.TRANSPARENT);

            // Hiển thị Stage mới
            updatePeopleStage.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @FXML
    private void clickSearchPeople(ActionEvent event){ //nút search trên peoplePane
        //Cần kiểm tra mỗi trường được nhập rồi select trong csdl, sau đó hiển thị vào table bên dưới

    }
    @FXML
    private void minimizePeople(ActionEvent event) { //nút minimize của mỗi stage addPeople và updatePeople
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void closePeople(ActionEvent event) { //nút close của mỗi stage addPeople và updatePeople
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        newStages.remove(stage);
        stage.close();
    }

    @FXML
    private void addAddPeople(ActionEvent event) { ////nút add của mỗi stage addPeople
        //Thực hiện khi nhấn add: cần kiểm tra các trường đã nhập, thêm vào cơ sở dữ liệu sau đó đóng stage(đóng stage gọi luôn closeAddPeople cho nhanh);

    }


    /*
        Hết quản lý nhân khẩu
     */

    /*
        Quản lý hộ khẩu
     */
    @FXML
    private void openHomePane(ActionEvent event) {
        peoplePane.setVisible(false);
        homePane.setVisible(true);
        moneyPane.setVisible(false);
    }

    @FXML
    void addNewMember(ActionEvent event) { //click add new để thêm dòng thêm thành viên
        Insets insets = new Insets(10, 20, 0, 20);
        HBox hbox = new HBox(10); // Khoảng cách giữa các phần tử trong HBox là 10
        Label cmnd = new Label("CMND:");
        TextField cmndText = new TextField();
        cmndText.setPromptText("CMND");
        Label qh = new Label("Quan hệ với chủ hộ:");
        TextField qhText = new TextField();
        qhText.setPromptText("Quan hệ với chủ hộ");
        Button addButton = new Button("add");
        addButton.getStyleClass().add("detail-btn");
        Button delButton = new Button("delete");
        delButton.getStyleClass().add("delete-btn");

        hbox.getChildren().addAll(cmnd, cmndText, qh, qhText, addButton, delButton);
        hbox.setPadding(insets);
        container.getChildren().add(hbox);
    }

    @FXML
    void clickDetailHouseHold(ActionEvent event) {//Nhấn detail trong scene quản lý hộ khẩu để mở stage mới là updateHome.fxml
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("updateHome.fxml"));
            Parent updateHomeRoot = loader.load();

            // Tạo một Stage mới
            Stage updateHomeStage = new Stage();
            updateHomeStage.setScene(new Scene(updateHomeRoot));
            newStages.add(updateHomeStage);

            //Cài đặt để có thể di chuyển stage bằng kéo thả
            updateHomeRoot.setOnMousePressed((MouseEvent e) -> {
                x = e.getScreenX() - updateHomeStage.getX();
                y = e.getScreenY() - updateHomeStage.getY();
            });

            updateHomeRoot.setOnMouseDragged((MouseEvent e) -> {
                updateHomeStage.setX(e.getScreenX() - x);
                updateHomeStage.setY(e.getScreenY() - y);
            });

            // Đặt kiểu modality của Stage mới là NONE
            updateHomeStage.initModality(Modality.NONE);
            updateHomeStage.initStyle(StageStyle.TRANSPARENT);

            // Hiển thị Stage mới
            updateHomeStage.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }



    @FXML
    void clickAddHouseHold(ActionEvent event) {//Nhấn add trong scene quản lý hộ khẩu để mở stage mới là addHome.fxml
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addHome.fxml"));
            Parent addHomeRoot = loader.load();

            // Tạo một Stage mới
            Stage addHomeStage = new Stage();
            addHomeStage.setScene(new Scene(addHomeRoot));
            newStages.add(addHomeStage);

            //Cài đặt để có thể di chuyển stage bằng kéo thả
            addHomeRoot.setOnMousePressed((MouseEvent e) -> {
                x = e.getScreenX() - addHomeStage.getX();
                y = e.getScreenY() - addHomeStage.getY();
            });

            addHomeRoot.setOnMouseDragged((MouseEvent e) -> {
                addHomeStage.setX(e.getScreenX() - x);
                addHomeStage.setY(e.getScreenY() - y);
            });

            // Đặt kiểu modality của Stage mới là NONE
            addHomeStage.initModality(Modality.NONE);
            addHomeStage.initStyle(StageStyle.TRANSPARENT);

            // Hiển thị Stage mới
            addHomeStage.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


    @FXML
    void clickSplitHouseHold(ActionEvent event) {//Nhấn split trong scene quản lý hộ khẩu để mở stage mới là splitHome.fxml
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("splitHome.fxml"));
            Parent splitHomeRoot = loader.load();

            // Tạo một Stage mới
            Stage splitHomeStage = new Stage();
            splitHomeStage.setScene(new Scene(splitHomeRoot));
            newStages.add(splitHomeStage);

            //Cài đặt để có thể di chuyển stage bằng kéo thả
            splitHomeRoot.setOnMousePressed((MouseEvent e) -> {
                x = e.getScreenX() - splitHomeStage.getX();
                y = e.getScreenY() - splitHomeStage.getY();
            });

            splitHomeRoot.setOnMouseDragged((MouseEvent e) -> {
                splitHomeStage.setX(e.getScreenX() - x);
                splitHomeStage.setY(e.getScreenY() - y);
            });

            // Đặt kiểu modality của Stage mới là NONE
            splitHomeStage.initModality(Modality.NONE);
            splitHomeStage.initStyle(StageStyle.TRANSPARENT);

            // Hiển thị Stage mới
            splitHomeStage.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    @FXML
    void clickAddMoney(ActionEvent event) {//Nhấn add trong scene quản lý thu chi để mở stage mới là addMoney.fxml
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addMoney.fxml"));
            Parent addMoneyRoot = loader.load();

            // Tạo một Stage mới
            Stage addMoneyStage = new Stage();
            addMoneyStage.setScene(new Scene(addMoneyRoot));
            newStages.add(addMoneyStage);

            //Cài đặt để có thể di chuyển stage bằng kéo thả
            addMoneyRoot.setOnMousePressed((MouseEvent e) -> {
                x = e.getScreenX() - addMoneyStage.getX();
                y = e.getScreenY() - addMoneyStage.getY();
            });

            addMoneyRoot.setOnMouseDragged((MouseEvent e) -> {
                addMoneyStage.setX(e.getScreenX() - x);
                addMoneyStage.setY(e.getScreenY() - y);
            });

            // Đặt kiểu modality của Stage mới là NONE
            addMoneyStage.initModality(Modality.NONE);
            addMoneyStage.initStyle(StageStyle.TRANSPARENT);

            // Hiển thị Stage mới
            addMoneyStage.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @FXML
    void clickAnalyzeMoney(ActionEvent event) {//Nhấn analyze trong scene quản lý thu chi để mở stage mới là analyzeMoney.fxml
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("analyzeMoney.fxml"));
            Parent analyzeMoneyRoot = loader.load();

            // Tạo một Stage mới
            Stage analyzeMoneyStage = new Stage();
            analyzeMoneyStage.setScene(new Scene(analyzeMoneyRoot));
            newStages.add(analyzeMoneyStage);

            //Cài đặt để có thể di chuyển stage bằng kéo thả
            analyzeMoneyRoot.setOnMousePressed((MouseEvent e) -> {
                x = e.getScreenX() - analyzeMoneyStage.getX();
                y = e.getScreenY() - analyzeMoneyStage.getY();
            });

            analyzeMoneyRoot.setOnMouseDragged((MouseEvent e) -> {
                analyzeMoneyStage.setX(e.getScreenX() - x);
                analyzeMoneyStage.setY(e.getScreenY() - y);
            });

            // Đặt kiểu modality của Stage mới là NONE
            analyzeMoneyStage.initModality(Modality.NONE);
            analyzeMoneyStage.initStyle(StageStyle.TRANSPARENT);

            // Hiển thị Stage mới
            analyzeMoneyStage.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }



    /*
        Hết quản lý hộ khẩu
     */

    /*
        Quản lý thu chi
     */
    @FXML
    private void openMoneyPane(ActionEvent event) {
        peoplePane.setVisible(false);
        homePane.setVisible(false);
        moneyPane.setVisible(true);
    }

    public void clickFeeRButton(ActionEvent actionEvent) {
    }

    public void clickDonateRButton(ActionEvent actionEvent) {
    }


    /*
        Hết Quản lý thu chi
     */
}
