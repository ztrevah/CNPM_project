package com.example.cnpm;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
    String selectedId; // CCCD của nhân khẩu được chọn trong bảng Quản lý nhân khẩu
    // Lưu CCCD của nhân khẩu được chọn trong bảng Quản lý nhân khẩu khi bấm vào 1 bản ghi
    @FXML
    void selectPerson(MouseEvent event) {
        selectedId = peopleTable.getSelectionModel().getSelectedItem().getSoCCCD();
    }
    // Mở cửa sổ sửa thông tin nhân khẩu chỉ khi đã chọn 1 người
    @FXML
    void clickDetailPeople(ActionEvent e) { //Nhấn detail trong scene quản lý nhân khẩu để mở stage mới là updatePeople.fxml
        if(selectedId != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("updatePeople.fxml"));
                Parent updatePeopleRoot = loader.load();

                // Tạo một Stage mới
                Stage updatePeopleStage = new Stage();
                updatePeopleStage.setScene(new Scene(updatePeopleRoot));
                newStages.add(updatePeopleStage);

                Menu updatePeopleController = loader.getController();
                updatePeopleController.updatePeopleCCCDField.setText(selectedId);
                updatePeopleController.updatePeopleCCCDField.setEditable(false);

                DatabaseConnector databaseConnector = new DatabaseConnector();
                databaseConnector.connect();
                ResultSet personalData = databaseConnector.getPeopleInfo(selectedId);
                while (personalData.next()) {
                    updatePeopleController.updatePeopleHoTenField.setText(personalData.getString("HoTen"));
                    updatePeopleController.updatePeopleBiDanhField.setText(personalData.getString("BiDanh"));
                    updatePeopleController.updatePeopleNgaySinhField.setValue(personalData.getDate("NgaySinh").toLocalDate());
                    updatePeopleController.updatePeopleNoiSinhField.setText(personalData.getString("NoiSinh"));
                    updatePeopleController.updatePeopleQueQuanField.setText(personalData.getString("QueQuan"));
                    updatePeopleController.updatePeopleGioiTinhField.setValue(personalData.getString("GioiTinh"));
                    updatePeopleController.updatePeopleDanTocField.setText(personalData.getString("DanToc"));
                    updatePeopleController.updatePeopleQuocTichField.setText(personalData.getString("QuocTich"));
                    updatePeopleController.updatePeopleNgheNghiepField.setText(personalData.getString("NgheNghiep"));
                    updatePeopleController.updatePeopleNoiLamViecField.setText(personalData.getString("NoiLamViec"));

                }
                databaseConnector.disconnect();

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
            } catch (IOException | SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    @FXML
    private TextField ageFrom;
    @FXML
    private TextField ageTo;
    @FXML
    private DatePicker endDate;
    @FXML
    private ComboBox<String> loaiLuuTru;
    @FXML
    private ComboBox<String> seachGioiTinh;
    @FXML
    private TextField searchIDNameField;
    @FXML
    private DatePicker startDate;
    @FXML
    void actionOnClickTinhTrang(MouseEvent event) {
        loaiLuuTru.setItems(FXCollections.observableArrayList("Tất cả","Thường trú","Tạm trú","Tạm vắng"));
    }
    @FXML
    void actionOnClickSearchSex(MouseEvent event) {
        seachGioiTinh.setItems(FXCollections.observableArrayList("Tất cả","Nam","Nữ"));
    }
    @FXML
    private TableView<Person> peopleTable;
    @FXML
    private TableColumn<Person,String> addressColumn;
    @FXML
    private TableColumn<Person,String> dobColumn;
    @FXML
    private TableColumn<Person,String> idColumn;
    @FXML
    private TableColumn<Person,String> nameColumn;
    @FXML
    private TableColumn<Person,String> sexColumn;
    @FXML
    private TableColumn<Person,String> stateColumn;
    // Tìm kiếm nhân khẩu theo các tiêu chí đã chọn
    @FXML
    private void clickSearchPeople(ActionEvent event){ //nút search trên peoplePane
        //Cần kiểm tra mỗi trường được nhập rồi select trong csdl, sau đó hiển thị vào table bên dưới
        peopleTable.getItems().clear();
        ObservableList<Person> dataList = FXCollections.observableArrayList();

        String queriedId = searchIDNameField.getText();
        String queriedName = searchIDNameField.getText();
        String queriedSex = seachGioiTinh.getValue();
        String tmp_queriedAgeFrom = ageFrom.getText();
        String tmp_queriedAgeTo = ageTo.getText();
        String queriedState = loaiLuuTru.getValue();
        LocalDate tmp_queriedFromDate = startDate.getValue();
        LocalDate tmp_queriedToDate = endDate.getValue();
        String queriedFromDate,queriedToDate;

        if(queriedSex == null || queriedSex.equals("Tất cả")) queriedSex = "";
        int queriedAgeFrom,queriedAgeTo;
        if(tmp_queriedAgeFrom.length() == 0) queriedAgeFrom = 0;
        else queriedAgeFrom = Integer.parseInt(tmp_queriedAgeFrom);
        if(tmp_queriedAgeTo.length() == 0) queriedAgeTo = 200;
        else queriedAgeTo = Integer.parseInt(tmp_queriedAgeTo);
        if(queriedState == null || queriedState.equals("Tất cả")) queriedState = "";
        if(tmp_queriedFromDate == null) queriedFromDate = "1900/1/1";
        else queriedFromDate = tmp_queriedFromDate.toString();
        if(tmp_queriedToDate == null) queriedToDate = "2100/1/1";
        else queriedToDate = tmp_queriedToDate.toString();

        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.connect();
        ResultSet resultSetPersonalInfo = databaseConnector.getPeopleInfoList(queriedId,queriedName,queriedSex,queriedAgeFrom,queriedAgeTo);

        try {
            while(resultSetPersonalInfo.next()) {
                String SoCCCD = resultSetPersonalInfo.getString(1);
                String HoTen = resultSetPersonalInfo.getString(2);
                Date NgaySinh = resultSetPersonalInfo.getDate(3);
                String GioiTinh = resultSetPersonalInfo.getString(4);

                ResultSet homeInfo = databaseConnector.getPersonAddress(SoCCCD,queriedState,queriedFromDate,queriedToDate);
                String NoiLuuTru = "",TinhTrangLuuTru = "";
                while(homeInfo.next()) {
                    String tmp_NoiLuuTru = homeInfo.getString(1);
                    NoiLuuTru = NoiLuuTru.concat(tmp_NoiLuuTru +'\n');
                    String tmp_LoaiLuuTru = homeInfo.getString(2);
                    String tmp_NgayBatDau = homeInfo.getDate(3).toString();
                    String tmp_NgayKetThuc = homeInfo.getDate(4).toString();
                    if(tmp_NgayKetThuc.equals("2100-01-01")) tmp_NgayKetThuc = "nay";
                    TinhTrangLuuTru = TinhTrangLuuTru.concat(tmp_LoaiLuuTru + " từ " + tmp_NgayBatDau + " đến " + tmp_NgayKetThuc + '\n');
                }

                if(!NoiLuuTru.isEmpty() || (NoiLuuTru.isEmpty() && queriedFromDate.equals("1900/1/1") && queriedToDate.equals("2100/1/1"))) {
                    Person person = new Person(SoCCCD, HoTen, NgaySinh.toString(), GioiTinh, NoiLuuTru, TinhTrangLuuTru);
                    dataList.add(person);
                }
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        idColumn.setCellValueFactory(new PropertyValueFactory<Person,String>("SoCCCD"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Person,String>("HoTen"));
        dobColumn.setCellValueFactory(new PropertyValueFactory<Person,String>("NgaySinh"));
        sexColumn.setCellValueFactory(new PropertyValueFactory<Person,String>("GioiTinh"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<Person,String>("NoiThuongTru"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<Person,String>("TinhTrangLuuTru"));

        peopleTable.setItems(dataList);
        databaseConnector.disconnect();
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
    private TextField addPeopleBiDanhField;
    @FXML
    private TextField addPeopleCMNDField;
    @FXML
    private TextField addPeopleDanTocField;
    @FXML
    private ComboBox<String> addPeopleGioiTinhField;
    @FXML
    private TextField addPeopleNoiSinhField;
    @FXML
    private TextField addPeopleHoTenField;
    @FXML
    private DatePicker addPeopleNgaySinhField;
    @FXML
    private TextField addPeopleNgheNghiepField;
    @FXML
    private TextField addPeopleNoiLamViecField;
    @FXML
    private TextField addPeopleQueQuanField;
    @FXML
    private TextField addPeopleQuocTichField;
    @FXML
    void actionOnClickGioiTinhField(MouseEvent event) {
        addPeopleGioiTinhField.setItems(FXCollections.observableArrayList("Nam","Nữ"));
    }
    // Nhấn thêm nhân khẩu khi hoàn thành nhập thông tin
    @FXML
    private void addAddPeople(ActionEvent event) { ////nút add của mỗi stage addPeople
        //Thực hiện khi nhấn add: cần kiểm tra các trường đã nhập, thêm vào cơ sở dữ liệu sau đó đóng stage(đóng stage gọi luôn closeAddPeople cho nhanh);
        String SoCCCD = addPeopleCMNDField.getText();
        String HoTen = addPeopleHoTenField.getText();
        String BiDanh = addPeopleBiDanhField.getText();
        LocalDate NgaySinh = addPeopleNgaySinhField.getValue();
        String NoiSinh = addPeopleNoiSinhField.getText();
        String GioiTinh = addPeopleGioiTinhField.getValue();
        String NgheNghiep = addPeopleNgheNghiepField.getText();
        String QueQuan = addPeopleQueQuanField.getText();
        String DanToc = addPeopleDanTocField.getText();
        String QuocTich = addPeopleQuocTichField.getText();
        String NoiLamViec = addPeopleNoiLamViecField.getText();

        if(SoCCCD.length() == 0) SoCCCD = null;
        if(HoTen.length() == 0) HoTen = null;
        if(BiDanh.length() == 0) BiDanh = null;
        if(NoiSinh.length() == 0) NoiSinh = null;
        if(NgheNghiep.length() == 0) NgheNghiep = null;
        if(QueQuan.length() == 0) QueQuan = null;
        if(DanToc.length() == 0) DanToc = null;
        if(QuocTich.length() == 0) QuocTich = null;
        if(NoiLamViec.length() == 0) NoiLamViec = null;

        if(!checkEmpty(SoCCCD) && !checkEmpty(HoTen) && NgaySinh != null && !checkEmpty(GioiTinh) && !checkEmpty(NoiSinh)
                && !checkEmpty(QuocTich) && !checkEmpty(QueQuan) && !checkEmpty(GioiTinh)) {
            DatabaseConnector databaseConnector = new DatabaseConnector();
            databaseConnector.connect();
            if(databaseConnector.checkExistNhanKhau(SoCCCD)) {
                Alert alert;
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Đã tồn tại nhân khẩu " + SoCCCD);
                alert.showAndWait();
            }
            else {
                databaseConnector.addPerson(SoCCCD,HoTen,BiDanh,NgaySinh.toString(),NoiSinh,GioiTinh,NgheNghiep,QueQuan,DanToc,QuocTich,NoiLamViec);
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Alert alert;
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Successful");
                alert.setHeaderText(null);
                alert.setContentText("Thêm thành công nhân khẩu " + SoCCCD);
                alert.showAndWait();
                stage.close();
            }
            databaseConnector.disconnect();
        }
        else {
            Alert alert;
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Nhập thiếu dữ liệu");
            alert.showAndWait();
        }
    }
    @FXML
    private TextField updatePeopleBiDanhField;

    @FXML
    private TextField updatePeopleCCCDField;

    @FXML
    private TextField updatePeopleDanTocField;

    @FXML
    private ComboBox<String> updatePeopleGioiTinhField;

    @FXML
    private TextField updatePeopleHoTenField;

    @FXML
    private DatePicker updatePeopleNgaySinhField;

    @FXML
    private TextField updatePeopleNgheNghiepField;

    @FXML
    private TextField updatePeopleNoiLamViecField;

    @FXML
    private TextField updatePeopleQueQuanField;

    @FXML
    private TextField updatePeopleQuocTichField;
    @FXML
    private TextField updatePeopleNoiSinhField;
    @FXML
    void actionOnClickGioiTinhField_updatePeople(MouseEvent event) {
        updatePeopleGioiTinhField.setItems(FXCollections.observableArrayList("Nam","Nữ"));
    }
    // Nhấn cập nhật thông tin nhân khẩu
    @FXML
    void actionOnClickUpdate(MouseEvent event) {
        String SoCCCD = updatePeopleCCCDField.getText();
        String HoTen = updatePeopleHoTenField.getText();
        String BiDanh = updatePeopleBiDanhField.getText();
        LocalDate NgaySinh = updatePeopleNgaySinhField.getValue();
        String NoiSinh = updatePeopleNoiSinhField.getText();
        String GioiTinh = updatePeopleGioiTinhField.getValue().toString();
        String NgheNghiep = updatePeopleNgheNghiepField.getText();
        String QueQuan = updatePeopleQueQuanField.getText();
        String DanToc = updatePeopleDanTocField.getText();
        String QuocTich = updatePeopleQuocTichField.getText();
        String NoiLamViec = updatePeopleNoiLamViecField.getText();

        if(checkEmpty(HoTen) && NgaySinh == null && checkEmpty(GioiTinh) && checkEmpty(NoiSinh)
                && checkEmpty(QuocTich) && checkEmpty(QueQuan) && checkEmpty(GioiTinh)) {
            DatabaseConnector databaseConnector = new DatabaseConnector();
            databaseConnector.connect();
            databaseConnector.updatePerson(SoCCCD,HoTen,BiDanh,NgaySinh.toString(),NoiSinh,GioiTinh,NgheNghiep,QueQuan,DanToc,QuocTich,NoiLamViec);
            databaseConnector.disconnect();
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Alert alert;
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful");
            alert.setHeaderText(null);
            alert.setContentText("Cập nhật thành công thông tin nhân khẩu " + SoCCCD);
            alert.showAndWait();
            stage.close();
        }
        else {
            Alert alert;
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Nhập thiếu dữ liệu");
            alert.showAndWait();
        }

    }
    public boolean checkEmpty(String s) {
        if(s == null || s.isEmpty()) return true;
        else return false;
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
    private TextField searchIDNameHome;
    @FXML
    private TableView<Home> homeTable;
    @FXML
    private TableColumn<Home,String> tenChuHoColumn;
    @FXML
    private TableColumn<Home,String> diaChiHoKhauColumn;
    @FXML
    private TableColumn<Home,String> idHoKhauColumn;
    // Tìm kiếm hộ khẩu theo tiêu chí đã chọn
    @FXML
    void clickSearchHome(MouseEvent event) {
        if(homeTable != null) homeTable.getItems().clear();
        ObservableList<Home> dataList = FXCollections.observableArrayList();

        String queriedIDNameHome = searchIDNameHome.getText();

        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.connect();
        ResultSet homeList = databaseConnector.getHomeList(queriedIDNameHome);

        try {
            while(homeList.next()) {
                String SoHK = homeList.getString(1);
                String TenChuHo = homeList.getString(3);
                String DiaChi = homeList.getString(4);

                Home home = new Home(SoHK,TenChuHo,DiaChi);
                dataList.add(home);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        idHoKhauColumn.setCellValueFactory(new PropertyValueFactory<Home,String>("SoHK"));
        tenChuHoColumn.setCellValueFactory(new PropertyValueFactory<Home,String>("TenChuHo"));
        diaChiHoKhauColumn.setCellValueFactory(new PropertyValueFactory<Home,String>("DiaChi"));

        homeTable.setItems(dataList);
        databaseConnector.disconnect();
    }
    public static String selectedIDHome; // Hộ khẩu được chọn
    @FXML
    void selectHome(MouseEvent event) {
        selectedIDHome = homeTable.getSelectionModel().getSelectedItem().getSoHK();
    }
    @FXML
    private TextField idHKFieldAddHome;
    @FXML
    private TextField idChuHoFieldAddHome;
    @FXML
    private TextField addressHomeFieldAddHome;
    @FXML
    private TableColumn<PeopleInfoOfHome, String> idAddHomeColumn;
    @FXML
    private TableColumn<PeopleInfoOfHome, String> nameAddHomeColumn;
    @FXML
    private TableColumn<PeopleInfoOfHome, String> relationAddHomeColumn;
    @FXML
    private TableView<PeopleInfoOfHome> tempHoKhauTableAddHome;
    // Làm mới bảng hiển thị danh sách tạm nhân khẩu của một hộ
    void refreshTempHoKhauTable(){
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.connect();
        ResultSet resultSet = databaseConnector.getTempHoKhauTable();
        if(tempHoKhauTableAddHome != null) tempHoKhauTableAddHome.getItems().clear();
        ObservableList<PeopleInfoOfHome> dataList = FXCollections.observableArrayList();
        try {
            while(resultSet.next()) {
                String SoCCCD = resultSet.getString(1);
                String HoTen = resultSet.getString(2);
                String QuanHeChuHo = resultSet.getString(3);
                PeopleInfoOfHome peopleInfoOfHome = new PeopleInfoOfHome(SoCCCD,HoTen,QuanHeChuHo);
                dataList.add(peopleInfoOfHome);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        idAddHomeColumn.setCellValueFactory(new PropertyValueFactory<PeopleInfoOfHome,String>("SoCCCD"));
        nameAddHomeColumn.setCellValueFactory(new PropertyValueFactory<PeopleInfoOfHome,String>("HoTen"));
        relationAddHomeColumn.setCellValueFactory(new PropertyValueFactory<PeopleInfoOfHome,String>("QuanHeChuHo"));
        tempHoKhauTableAddHome.setItems(dataList);
        databaseConnector.disconnect();
    }
    // Hàm xử lý khi ấn Add new và xử lý khi ấn add và delete
    @FXML
    void addNewMemberNewHome(ActionEvent event) {
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
        // Hàm xử lý khi ấn add: Thêm người vào bảng tạm
        addButton.setOnMouseClicked((MouseEvent e) -> {
            if(!cmndText.getText().isEmpty() && !qhText.getText().isEmpty()) {
                String id = cmndText.getText();
                String QuanHeChuHo = qhText.getText();
                DatabaseConnector databaseConnector = new DatabaseConnector();
                databaseConnector.connect();
                // Nếu người định thêm chưa có trong danh sách nhân khẩu thì mở giao diện addPeople để tạo mới
                if(!databaseConnector.checkExistNhanKhauInNhanKhauList(id)){
                    try {
                        Alert alert;
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Warning");
                        alert.setHeaderText(null);
                        alert.setContentText("Người này chưa có trong danh sách nhân khẩu. Yêu cầu thêm mới");
                        alert.showAndWait();

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("addPeople.fxml"));
                        Parent addPeopleRoot = loader.load();

                        // Tạo một Stage mới
                        Stage addPeopleStage = new Stage();
                        addPeopleStage.setScene(new Scene(addPeopleRoot));
                        newStages.add(addPeopleStage);

                        Menu addPeopleController = loader.getController();
                        addPeopleController.addPeopleCMNDField.setText(id);
                        addPeopleController.addPeopleCMNDField.setEditable(false);

                        //Cài đặt để có thể di chuyển stage bằng kéo thả
                        addPeopleRoot.setOnMousePressed((MouseEvent event1) -> {
                            x = event1.getScreenX() - addPeopleStage.getX();
                            y = event1.getScreenY() - addPeopleStage.getY();
                        });

                        addPeopleRoot.setOnMouseDragged((MouseEvent event1) -> {
                            addPeopleStage.setX(event1.getScreenX() - x);
                            addPeopleStage.setY(event1.getScreenY() - y);
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
                else {
                    // Nếu người được thêm đang là chủ hộ của hộ khác thì phải thêm cả hộ hoặc thay đổi chủ hộ
                    if(!databaseConnector.checkExistChuHoInHoKhauList(id)) {
                        String HoTen = databaseConnector.getHoTen(id);
                        // Nếu ấn add đối với người đã thêm vào bảng tạm thì chỉ cập nhật quan hệ chủ hộ
                        if(databaseConnector.checkExistNhanKhauTempHoKhauTable(id))
                            databaseConnector.updateRelationTempHoKhauTable(id,QuanHeChuHo);
                        else databaseConnector.insertNewMemberTempHoKhauTable(id,HoTen,QuanHeChuHo);
                    }
                    else {
                        Alert alert;
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Không được thêm chủ hộ của hộ khác");
                        alert.showAndWait();
                    }
                }
                databaseConnector.disconnect();
                refreshTempHoKhauTable();
            }
            else {
                Alert alert;
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Chưa đủ dữ liệu");
                alert.showAndWait();
            }

        });
        delButton.setOnMouseClicked((MouseEvent e) -> {
            if(!cmndText.getText().isEmpty()) {
                String id = cmndText.getText();
                DatabaseConnector databaseConnector = new DatabaseConnector();
                databaseConnector.connect();
                databaseConnector.deleteMemberFromTempHoKhauTable(id);
                databaseConnector.disconnect();
                refreshTempHoKhauTable();
            }
            else {
                Alert alert;
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Chưa đủ dữ liệu");
                alert.showAndWait();
            }
        });

        hbox.getChildren().addAll(cmnd, cmndText, qh, qhText, addButton, delButton);
        hbox.setPadding(insets);
        container.getChildren().add(hbox);
    }
    // Bấm done để hoàn thành thêm hộ khẩu mới
    @FXML
    void finishAddNewHome(MouseEvent event) {
        String HoKhauID = idHKFieldAddHome.getText();
        String ChuHoID = idChuHoFieldAddHome.getText();
        String DiaChi = addressHomeFieldAddHome.getText();
        // Các trường mã hộ khẩu, số cccd của chủ hộ, địa chỉ không được để trống
        if(!HoKhauID.isEmpty() && !ChuHoID.isEmpty() && !DiaChi.isEmpty()) {
            DatabaseConnector databaseConnector = new DatabaseConnector();
            databaseConnector.connect();
            // Nếu chủ hộ đã có trong danh sách nhân khẩu của hộ và không là chủ hộ của hộ khác và mã hộ khẩu chưa tồn tại
            // thì tạo hộ khẩu mới với những người trong danh sách tạm
            if(!databaseConnector.checkExistChuHoInHoKhauList(ChuHoID) && !databaseConnector.checkExistHoKhauInHoKhauList(HoKhauID)
                    && databaseConnector.checkExistChuHoInTempHoKhauTable(ChuHoID))
            {
                databaseConnector.insertNewHoKhau(HoKhauID,ChuHoID,DiaChi);
                ResultSet peopleListInsertIntoNewHoKhau = databaseConnector.getTempHoKhauTable();
                try{
                    while(peopleListInsertIntoNewHoKhau.next()) {
                        String NhanKhauID = peopleListInsertIntoNewHoKhau.getString("CCCD");
                        String QuanHeChuHo = peopleListInsertIntoNewHoKhau.getString("QuanHeChuHo");
                        if(databaseConnector.checkExistNhanKhauThuongTru(NhanKhauID))
                            databaseConnector.updateNgayKetThucThuongTru(NhanKhauID);
                        databaseConnector.insertNewNhanKhauHoKhau(NhanKhauID,HoKhauID,(new Date(System.currentTimeMillis())).toString(),
                                "2100-1-1","Thường trú",QuanHeChuHo);
                    }

                    Alert alert;
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successful");
                    alert.setHeaderText(null);
                    alert.setContentText("Tạo thành công hộ khẩu mới");
                    alert.showAndWait();
                    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    newStages.remove(stage);
                    stage.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else {
                if(databaseConnector.checkExistChuHoInHoKhauList(ChuHoID)) {
                    Alert alert;
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Không được thêm chủ hộ của hộ khác ");
                    alert.showAndWait();
                }
                if(databaseConnector.checkExistHoKhauInHoKhauList(HoKhauID)) {
                    Alert alert;
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Đã tồn tại số hộ khẩu này ");
                    alert.showAndWait();
                }
                if(!databaseConnector.checkExistChuHoInTempHoKhauTable(ChuHoID)) {
                    Alert alert;
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Chủ hộ chưa được thêm vào hộ khẩu này");
                    alert.showAndWait();
                }

            }
            databaseConnector.disconnect();

        }
        else {
            Alert alert;
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Nhập thiếu dữ liệu");
            alert.showAndWait();
        }

    }
    @FXML
    void cancelAddNewHome(MouseEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        newStages.remove(stage);
        stage.close();
    }

    // Cập nhật thông tin của hộ khẩu đã có
    @FXML
    void clickDetailHouseHold(ActionEvent event) {//Nhấn detail trong scene quản lý hộ khẩu để mở stage mới là updateHome.fxml
        if(selectedIDHome != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("updateHome.fxml"));
                Parent updateHomeRoot = loader.load();

                // Tạo một Stage mới
                Stage updateHomeStage = new Stage();
                updateHomeStage.setScene(new Scene(updateHomeRoot));
                newStages.add(updateHomeStage);

                Menu updateHomeController = loader.getController();
                updateHomeController.idHKFieldUpdateHome.setText(selectedIDHome);
                updateHomeController.idHKFieldUpdateHome.setEditable(false);

                DatabaseConnector databaseConnector = new DatabaseConnector();
                databaseConnector.connect();
                databaseConnector.deleteTempHoKhauTable();

                ResultSet homeInfo = databaseConnector.getHomeInfo(selectedIDHome);
                while(homeInfo.next()) {
                    updateHomeController.idChuHoFieldUpdateHome.setText(homeInfo.getString(2));
                    updateHomeController.addressHomeFieldUpdateHome.setText(homeInfo.getString(3));
                }

                ResultSet memberList = databaseConnector.getCurrentMember(selectedIDHome);
                while(memberList.next()) {
                    String tmp_CCCD = memberList.getString(1);
                    String tmp_relation = memberList.getString(2);
                    String tmp_hoten = databaseConnector.getHoTen(tmp_CCCD);
                    databaseConnector.insertNewMemberTempHoKhauTable(tmp_CCCD,tmp_hoten,tmp_relation);
                }
                ResultSet resultSet = databaseConnector.getTempHoKhauTable();
                if(updateHomeController.tempHoKhauTableUpdateHome != null)
                    updateHomeController.tempHoKhauTableUpdateHome.getItems().clear();
                ObservableList<PeopleInfoOfHome> dataList = FXCollections.observableArrayList();
                try {
                    while(resultSet.next()) {
                        String SoCCCD = resultSet.getString(1);
                        String HoTen = resultSet.getString(2);
                        String QuanHeChuHo = resultSet.getString(3);
                        PeopleInfoOfHome peopleInfoOfHome = new PeopleInfoOfHome(SoCCCD,HoTen,QuanHeChuHo);
                        dataList.add(peopleInfoOfHome);
                    }
                } catch(SQLException e) {
                    e.printStackTrace();
                }
                updateHomeController.idUpdateHomeColumn.setCellValueFactory(new PropertyValueFactory<PeopleInfoOfHome,String>("SoCCCD"));
                updateHomeController.nameUpdateHomeColumn.setCellValueFactory(new PropertyValueFactory<PeopleInfoOfHome,String>("HoTen"));
                updateHomeController.relationUpdateHomeColumn.setCellValueFactory(new PropertyValueFactory<PeopleInfoOfHome,String>("QuanHeChuHo"));
                updateHomeController.tempHoKhauTableUpdateHome.setItems(dataList);
                databaseConnector.disconnect();

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
            } catch (SQLException| IOException ioe) {
                ioe.printStackTrace();
            }
        }

    }
    @FXML
    private TextField idHKFieldUpdateHome;
    @FXML
    private TextField idChuHoFieldUpdateHome;
    @FXML
    private TextField addressHomeFieldUpdateHome;
    @FXML
    private TableColumn<PeopleInfoOfHome, String> idUpdateHomeColumn;
    @FXML
    private TableColumn<PeopleInfoOfHome, String> nameUpdateHomeColumn;
    @FXML
    private TableColumn<PeopleInfoOfHome, String> relationUpdateHomeColumn;
    @FXML
    private TableView<PeopleInfoOfHome> tempHoKhauTableUpdateHome;
    void refreshTempHoKhauUpdateHomeTable(){
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.connect();
        ResultSet resultSet = databaseConnector.getTempHoKhauTable();
        if(tempHoKhauTableUpdateHome != null) tempHoKhauTableUpdateHome.getItems().clear();
        ObservableList<PeopleInfoOfHome> dataList = FXCollections.observableArrayList();
        try {
            while(resultSet.next()) {
                String SoCCCD = resultSet.getString(1);
                String HoTen = resultSet.getString(2);
                String QuanHeChuHo = resultSet.getString(3);
                PeopleInfoOfHome peopleInfoOfHome = new PeopleInfoOfHome(SoCCCD,HoTen,QuanHeChuHo);
                dataList.add(peopleInfoOfHome);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        idUpdateHomeColumn.setCellValueFactory(new PropertyValueFactory<PeopleInfoOfHome,String>("SoCCCD"));
        nameUpdateHomeColumn.setCellValueFactory(new PropertyValueFactory<PeopleInfoOfHome,String>("HoTen"));
        relationUpdateHomeColumn.setCellValueFactory(new PropertyValueFactory<PeopleInfoOfHome,String>("QuanHeChuHo"));
        tempHoKhauTableUpdateHome.setItems(dataList);
        databaseConnector.disconnect();
    }
    @FXML
    void addNewMemberUpdateHome(MouseEvent event) {
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
        addButton.setOnMouseClicked((MouseEvent e) -> {
            // Nếu điền đủ CCCD và quan hệ chủ hộ thì add vào bảng tạm danh sách nhân khẩu
            if(!cmndText.getText().isEmpty() && !qhText.getText().isEmpty()) {
                String id = cmndText.getText();
                String QuanHeChuHo = qhText.getText();
                DatabaseConnector databaseConnector = new DatabaseConnector();
                databaseConnector.connect();
                // Nếu người được thêm chưa có trong danh sách nhân khẩu thì phải thêm vào trước
                if(!databaseConnector.checkExistNhanKhauInNhanKhauList(id)){
                    try {
                        Alert alert;
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Warning");
                        alert.setHeaderText(null);
                        alert.setContentText("Người này chưa có trong danh sách nhân khẩu. Yêu cầu thêm mới");
                        alert.showAndWait();

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("addPeople.fxml"));
                        Parent addPeopleRoot = loader.load();

                        // Tạo một Stage mới
                        Stage addPeopleStage = new Stage();
                        addPeopleStage.setScene(new Scene(addPeopleRoot));
                        newStages.add(addPeopleStage);

                        Menu addPeopleController = loader.getController();
                        addPeopleController.addPeopleCMNDField.setText(id);
                        addPeopleController.addPeopleCMNDField.setEditable(false);

                        //Cài đặt để có thể di chuyển stage bằng kéo thả
                        addPeopleRoot.setOnMousePressed((MouseEvent event1) -> {
                            x = event1.getScreenX() - addPeopleStage.getX();
                            y = event1.getScreenY() - addPeopleStage.getY();
                        });

                        addPeopleRoot.setOnMouseDragged((MouseEvent event1) -> {
                            addPeopleStage.setX(event1.getScreenX() - x);
                            addPeopleStage.setY(event1.getScreenY() - y);
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
                else {
                    // Nếu thêm chủ hộ của 1 hộ khác thì phải thêm cả hộ đó hoặc thay đổi chủ hộ của hộ đó
                    if(databaseConnector.checkExistChuHoInHoKhauList(id) && !databaseConnector.checkChuHoHoKhau(id,selectedIDHome)) {
                        Alert alert;
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Không được thêm chủ hộ của hộ khác");
                        alert.showAndWait();

                    }
                    else {
                        String HoTen = databaseConnector.getHoTen(id);
                        // Nếu ấn add đối với người đã thêm vào bảng tạm thì chỉ cập nhật quan hệ chủ hộ
                        if(databaseConnector.checkExistNhanKhauTempHoKhauTable(id))
                            databaseConnector.updateRelationTempHoKhauTable(id,QuanHeChuHo);
                        else databaseConnector.insertNewMemberTempHoKhauTable(id,HoTen,QuanHeChuHo);
                    }
                }
                databaseConnector.disconnect();
                refreshTempHoKhauUpdateHomeTable();
            }
            else {
                Alert alert;
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Chưa đủ dữ liệu");
                alert.showAndWait();
            }

        });
        delButton.setOnMouseClicked((MouseEvent e) -> {
            if(!cmndText.getText().isEmpty()) {
                String id = cmndText.getText();
                DatabaseConnector databaseConnector = new DatabaseConnector();
                databaseConnector.connect();
                databaseConnector.deleteMemberFromTempHoKhauTable(id);
                databaseConnector.disconnect();
                refreshTempHoKhauUpdateHomeTable();
            }
            else {
                Alert alert;
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Chưa đủ dữ liệu");
                alert.showAndWait();
            }
        });


        hbox.getChildren().addAll(cmnd, cmndText, qh, qhText, addButton, delButton);
        hbox.setPadding(insets);
        container.getChildren().add(hbox);
    }
    @FXML
    void finishUpdateHome(MouseEvent event) {
        String HoKhauID = idHKFieldUpdateHome.getText();
        String ChuHoID = idChuHoFieldUpdateHome.getText();
        String DiaChi = addressHomeFieldUpdateHome.getText();
        if(!ChuHoID.isEmpty() && !DiaChi.isEmpty()) {
            DatabaseConnector databaseConnector = new DatabaseConnector();
            databaseConnector.connect();
            // Nếu chủ hộ không nằm trong danh sách nhân khẩu mới
            if(!databaseConnector.checkExistChuHoInTempHoKhauTable(ChuHoID)) {
                Alert alert;
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Chưa có chủ hộ trong danh sách của hộ khẩu");
                alert.showAndWait();
            }
            else {
                databaseConnector.updateOwnerAddressHome(HoKhauID,ChuHoID,DiaChi); // Cập nhật chủ hộ
                ResultSet newMemberList = databaseConnector.getTempHoKhauTable(); // danh sách nhân khẩu mới của hộ khẩu
                ResultSet currentMemberList = databaseConnector.getCurrentMember(HoKhauID);
                try {
                    while(currentMemberList.next()) {
                        String NhanKhauID = currentMemberList.getString(1);
                        if(!databaseConnector.checkExistNhanKhauTempHoKhauTable(NhanKhauID))
                            databaseConnector.updateNgayKetThucThuongTru(NhanKhauID);
                    }
                    while(newMemberList.next()) {
                        String idNewMember = newMemberList.getString("CCCD");
                        String relationNewMember = newMemberList.getString("QuanHeChuHo");
                        // Nếu người được thêm đã có trong hộ khẩu từ trước thì chỉ update quan hệ chủ hộ
                        if(databaseConnector.checkExistNhanKhauHoKhau(idNewMember,HoKhauID))
                            databaseConnector.updateRelation(idNewMember,HoKhauID,relationNewMember);
                        else {
                            // Nếu người được thêm đang thường trú ở nơi khác thì kết thúc thường trú ở đó
                            if(databaseConnector.checkExistNhanKhauThuongTru(idNewMember))
                                databaseConnector.updateNgayKetThucThuongTru(idNewMember);
                            // Thêm bản ghi về thường trú của người được thêm ở hộ đang sửa vào bảng nhankhau_hokhau
                            databaseConnector.insertNewNhanKhauHoKhau(idNewMember,HoKhauID,(new Date(System.currentTimeMillis())).toString(),
                                    "2100-01-01","Thường trú",relationNewMember);
                        }

                    }
                    Alert alert;
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successful");
                    alert.setHeaderText(null);
                    alert.setContentText("Cập nhật hộ khẩu " + HoKhauID + " thành công!");
                    alert.showAndWait();
                    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    newStages.remove(stage);
                    stage.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }

            databaseConnector.disconnect();

        }
        else {
            Alert alert;
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Nhập thiếu dữ liệu");
            alert.showAndWait();
        }

    }
    @FXML
    void cancelUpdateHome(MouseEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        newStages.remove(stage);
        stage.close();
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
            DatabaseConnector databaseConnector = new DatabaseConnector();
            databaseConnector.connect();
            databaseConnector.deleteTempHoKhauTable();
            databaseConnector.disconnect();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }



    @FXML
    private TextField addressFieldSplitHome;
    @FXML
    private TextField addressNewHomeSplitHome;

    @FXML
    private TextField idChuHoFieldSplitHome;

    @FXML
    private TextField idHKFieldSplitHome;

    @FXML
    private TextField idNewChuHoFieldSplitHome;

    @FXML
    private TextField idNewHKFieldSplitHome;

    @FXML
    private TableColumn<PeopleInfoOfHome, String> idSplitMember;

    @FXML
    private TableColumn<PeopleInfoOfHome, String> nameSplitMember;

    @FXML
    private TableColumn<PeopleInfoOfHome, String> relationSplitMember;

    void refreshTempSplitHomeTable(Menu controller){
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.connect();
        ResultSet resultSet = databaseConnector.getTempHoKhauTable();
        if(controller.tempSplitHomeTable != null) controller.tempSplitHomeTable.getItems().clear();
        ObservableList<PeopleInfoOfHome> dataList = FXCollections.observableArrayList();
        try {
            while(resultSet.next()) {
                String SoCCCD = resultSet.getString(1);
                String HoTen = resultSet.getString(2);
                String QuanHeChuHo = resultSet.getString(3);
                PeopleInfoOfHome peopleInfoOfHome = new PeopleInfoOfHome(SoCCCD,HoTen,QuanHeChuHo);
                dataList.add(peopleInfoOfHome);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        controller.idSplitMember.setCellValueFactory(new PropertyValueFactory<PeopleInfoOfHome,String>("SoCCCD"));
        controller.nameSplitMember.setCellValueFactory(new PropertyValueFactory<PeopleInfoOfHome,String>("HoTen"));
        controller.relationSplitMember.setCellValueFactory(new PropertyValueFactory<PeopleInfoOfHome,String>("QuanHeChuHo"));
        controller.tempSplitHomeTable.setItems(dataList);
        databaseConnector.disconnect();
    }

    @FXML
    private TableView<PeopleInfoOfHome> tempSplitHomeTable;
    // Mở cửa sổ tách hộ khẩu, thêm danh sách các thành viên hiện tại vào phần thành viên
    @FXML
    void clickSplitHouseHold(ActionEvent event) {//Nhấn split trong scene quản lý hộ khẩu để mở stage mới là splitHome.fxml
        if(selectedIDHome != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("splitHome.fxml"));
                Parent splitHomeRoot = loader.load();

                // Tạo một Stage mới
                Stage splitHomeStage = new Stage();
                splitHomeStage.setScene(new Scene(splitHomeRoot));
                newStages.add(splitHomeStage);

                DatabaseConnector databaseConnector = new DatabaseConnector();
                databaseConnector.connect();
                databaseConnector.deleteTempHoKhauTable();

                Menu splitHomeController = loader.getController();
                ResultSet homeInfo = databaseConnector.getHomeInfo(selectedIDHome);
                // setText cho các trường mã hộ khẩu, cccd chủ hộ và địa chỉ của hộ cha và setText cho địa chỉ hộ tách ra là địa chỉ của hộ cha
                // các trường này đều không được sửa
                try {
                    splitHomeController.idHKFieldSplitHome.setText(selectedIDHome);
                    splitHomeController.idHKFieldSplitHome.setEditable(false);
                    while(homeInfo.next()) {
                        splitHomeController.idChuHoFieldSplitHome.setText(homeInfo.getString(2));
                        splitHomeController.idChuHoFieldSplitHome.setEditable(false);
                        splitHomeController.addressFieldSplitHome.setText(homeInfo.getString(3));
                        splitHomeController.addressFieldSplitHome.setEditable(false);
                        splitHomeController.addressNewHomeSplitHome.setText(homeInfo.getString(3));
                        splitHomeController.addressNewHomeSplitHome.setEditable(false);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                databaseConnector.disconnect();

                DatabaseConnector databaseConnector1 = new DatabaseConnector();
                databaseConnector1.connect();
                ResultSet currentMemberList = databaseConnector1.getCurrentMember(selectedIDHome);

                try {
                    // Thêm những thành viên của hộ vào phần thành viên
                    while(currentMemberList.next()) {
                        String idMember = currentMemberList.getString(1);
                        String relationMember = currentMemberList.getString(2);

                        Insets insets = new Insets(10, 20, 0, 20);
                        HBox hbox = new HBox(10); // Khoảng cách giữa các phần tử trong HBox là 10
                        Label cmnd = new Label("CMND:");
                        TextField cmndText = new TextField();
                        cmndText.setText(idMember);
                        cmndText.setEditable(false);
                        Label qh = new Label("Quan hệ với chủ hộ:");
                        TextField qhText = new TextField();
                        qhText.setText(relationMember);
                        // Không cho phép sửa quan hệ chủ hộ của hộ cha
                        if(databaseConnector1.checkChuHoHoKhau(idMember,selectedIDHome)) qhText.setEditable(false);
                        Button addButton = new Button("add");
                        addButton.getStyleClass().add("detail-btn");
                        Button delButton = new Button("delete");
                        delButton.getStyleClass().add("delete-btn");
                        addButton.setOnMouseClicked((MouseEvent e) -> {
                            if(!qhText.getText().isEmpty()) {
                                String id = cmndText.getText();
                                String relation = qhText.getText();
                                DatabaseConnector databaseConnector2 = new DatabaseConnector();
                                databaseConnector2.connect();
                                if(databaseConnector2.checkChuHoHoKhau(idMember,selectedIDHome)) {
                                    Alert alert;
                                    alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Error");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Không được tách chủ hộ");
                                    alert.showAndWait();
                                }
                                else {
                                    if(databaseConnector2.checkExistNhanKhauTempHoKhauTable(id))
                                        databaseConnector2.updateRelationTempHoKhauTable(id,relation);
                                    else {
                                        String name = databaseConnector2.getHoTen(id);
                                        databaseConnector2.insertNewMemberTempHoKhauTable(id,name,relation);
                                    }
                                    databaseConnector2.disconnect();
                                    refreshTempSplitHomeTable(splitHomeController);
                                }
                            }
                            else {
                                Alert alert;
                                alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Error");
                                alert.setHeaderText(null);
                                alert.setContentText("Chưa đủ dữ liệu");
                                alert.showAndWait();
                            }
                        });
                        delButton.setOnMouseClicked((MouseEvent e) -> {
                            String id = cmndText.getText();
                            DatabaseConnector databaseConnector2 = new DatabaseConnector();
                            databaseConnector2.connect();
                            databaseConnector2.deleteMemberFromTempHoKhauTable(id);
                            databaseConnector2.disconnect();
                            refreshTempSplitHomeTable(splitHomeController);
                        });

                        hbox.getChildren().addAll(cmnd, cmndText, qh, qhText, addButton, delButton);
                        hbox.setPadding(insets);
                        splitHomeController.container.getChildren().add(hbox);
                    }
                } catch(SQLException e) {
                    e.printStackTrace();
                }
                databaseConnector1.disconnect();
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
    }
    @FXML
    void cancelSplitHome(MouseEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        newStages.remove(stage);
        stage.close();
    }
    // Ấn hoàn thành tách hộ khẩu
    @FXML
    void finishSplitHome(MouseEvent event) {
        String HoKhauID = idNewHKFieldSplitHome.getText();
        String ChuHoID = idNewChuHoFieldSplitHome.getText();
        String DiaChi = addressNewHomeSplitHome.getText();
        // Nếu đã nhập đủ dữ liệu của hộ đc tách ra
        if(!HoKhauID.isEmpty() && !ChuHoID.isEmpty()) {
            DatabaseConnector databaseConnector = new DatabaseConnector();
            databaseConnector.connect();
            // Mã của hộ khẩu mới không được đã tồn tại và chủ hộ của hộ mới phải được có trong danh sách tạm
            if(!databaseConnector.checkExistHoKhauInHoKhauList(HoKhauID) && databaseConnector.checkExistChuHoInTempHoKhauTable(ChuHoID))
            {
                databaseConnector.insertNewHoKhau(HoKhauID,ChuHoID,DiaChi);
                ResultSet peopleListInsertIntoNewHoKhau = databaseConnector.getTempHoKhauTable();
                try{
                    while(peopleListInsertIntoNewHoKhau.next()) {
                        String NhanKhauID = peopleListInsertIntoNewHoKhau.getString("CCCD");
                        String QuanHeChuHo = peopleListInsertIntoNewHoKhau.getString("QuanHeChuHo");
                        // Xoá đki thường trú của các nhân khẩu được tách ra
                        if(databaseConnector.checkExistNhanKhauThuongTru(NhanKhauID))
                            databaseConnector.updateNgayKetThucThuongTru(NhanKhauID);

                        databaseConnector.insertNewNhanKhauHoKhau(NhanKhauID,HoKhauID,(new Date(System.currentTimeMillis())).toString(),
                                "2100-1-1","Thường trú",QuanHeChuHo);
                    }

                    Alert alert;
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successful");
                    alert.setHeaderText(null);
                    alert.setContentText("Tách thành công hộ khẩu mới");
                    alert.showAndWait();
                    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    newStages.remove(stage);
                    stage.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else {
                if(databaseConnector.checkExistHoKhauInHoKhauList(HoKhauID)) {
                    Alert alert;
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Đã tồn tại số hộ khẩu này ");
                    alert.showAndWait();
                }
                if(!databaseConnector.checkExistChuHoInTempHoKhauTable(ChuHoID)) {
                    Alert alert;
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Chủ hộ của hộ được tách ra chưa có trong danh sách được tách ra");
                    alert.showAndWait();
                }

            }
            databaseConnector.disconnect();

        }
        else {
            Alert alert;
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Nhập thiếu dữ liệu của hộ mới tách ra");
            alert.showAndWait();
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
