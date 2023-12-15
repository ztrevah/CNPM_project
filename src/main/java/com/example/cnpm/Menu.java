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
    String selectedId;
    @FXML
    void selectPerson(MouseEvent event) {
        selectedId = peopleTable.getSelectionModel().getSelectedItem().getSoCCCD();
    }
    @FXML
    void clickDetailPeople(ActionEvent e) { //Nhấn detail trong scene quản lý nhân khẩu để mở stage mới là updatePeople.fxml
        if(!selectedId.isEmpty()) {
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

                if(!NoiLuuTru.isEmpty()) {
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
    @FXML
    private void addAddPeople(ActionEvent event) { ////nút add của mỗi stage addPeople
        //Thực hiện khi nhấn add: cần kiểm tra các trường đã nhập, thêm vào cơ sở dữ liệu sau đó đóng stage(đóng stage gọi luôn closeAddPeople cho nhanh);
        String SoCCCD = addPeopleCMNDField.getText();
        String HoTen = addPeopleHoTenField.getText();
        String BiDanh = addPeopleBiDanhField.getText();
        String NgaySinh = addPeopleNgaySinhField.getValue().toString();
        String NoiSinh = addPeopleNoiSinhField.getText();
        String GioiTinh = addPeopleGioiTinhField.getValue().toString();
        String NgheNghiep = addPeopleNgheNghiepField.getText();
        String QueQuan = addPeopleQueQuanField.getText();
        String DanToc = addPeopleDanTocField.getText();
        String QuocTich = addPeopleQuocTichField.getText();
        String NoiLamViec = addPeopleNoiLamViecField.getText();

        if(SoCCCD.length() == 0) SoCCCD = null;
        if(HoTen.length() == 0) HoTen = null;
        if(BiDanh.length() == 0) BiDanh = null;
        if(NgaySinh.length() == 0) NgaySinh = null;
        if(NoiSinh.length() == 0) NoiSinh = null;
        if(GioiTinh.length() == 0) GioiTinh = null;
        if(NgheNghiep.length() == 0) NgheNghiep = null;
        if(QueQuan.length() == 0) QueQuan = null;
        if(DanToc.length() == 0) DanToc = null;
        if(QuocTich.length() == 0) QuocTich = null;
        if(NoiLamViec.length() == 0) NoiLamViec = null;

        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.connect();
        databaseConnector.addPerson(SoCCCD,HoTen,BiDanh,NgaySinh,NoiSinh,GioiTinh,NgheNghiep,QueQuan,DanToc,QuocTich,NoiLamViec);

        databaseConnector.disconnect();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
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
    @FXML
    void actionOnClickUpdate(MouseEvent event) {
        String SoCCCD = updatePeopleCCCDField.getText();
        String HoTen = updatePeopleHoTenField.getText();
        String BiDanh = updatePeopleBiDanhField.getText();
        String NgaySinh = updatePeopleNgaySinhField.getValue().toString();
        String NoiSinh = updatePeopleNoiSinhField.getText();
        String GioiTinh = updatePeopleGioiTinhField.getValue().toString();
        String NgheNghiep = updatePeopleNgheNghiepField.getText();
        String QueQuan = updatePeopleQueQuanField.getText();
        String DanToc = updatePeopleDanTocField.getText();
        String QuocTich = updatePeopleQuocTichField.getText();
        String NoiLamViec = updatePeopleNoiLamViecField.getText();


        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.connect();
        databaseConnector.updatePerson(SoCCCD,HoTen,BiDanh,NgaySinh,NoiSinh,GioiTinh,NgheNghiep,QueQuan,DanToc,QuocTich,NoiLamViec);

        databaseConnector.disconnect();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
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
    public String selectedIDHome;
    @FXML
    void selectHome(MouseEvent event) {
        selectedIDHome = homeTable.getSelectionModel().getSelectedItem().getSoHK();
    }
    @FXML
    void clickAddHome(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addHome.fxml"));
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
            DatabaseConnector databaseConnector = new DatabaseConnector();
            databaseConnector.connect();
            databaseConnector.deleteTempHoKhauTable();
            databaseConnector.disconnect();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
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
        addButton.setOnMouseClicked((MouseEvent e) -> {
            if(!cmndText.getText().isEmpty() && !qhText.getText().isEmpty()) {
                String id = cmndText.getText();
                String QuanHeChuHo = qhText.getText();
                DatabaseConnector databaseConnector = new DatabaseConnector();
                databaseConnector.connect();
                if(!databaseConnector.checkExistNhanKhauInNhanKhauList(id)){
                    try {
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
                    if(!databaseConnector.checkExistChuHoInHoKhauList(id)) {
                        String HoTen = databaseConnector.getHoTen(id);
                        databaseConnector.insertNewMemberTempHoKhauTable(id,HoTen,QuanHeChuHo);
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
            if(!cmndText.getText().isEmpty() && !qhText.getText().isEmpty()) {
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
    @FXML
    void finishAddNewHome(MouseEvent event) {
        String HoKhauID = idHKFieldAddHome.getText();
        String ChuHoID = idChuHoFieldAddHome.getText();
        String DiaChi = addressHomeFieldAddHome.getText();
        if(!HoKhauID.isEmpty() && !ChuHoID.isEmpty() && !DiaChi.isEmpty()) {
            DatabaseConnector databaseConnector = new DatabaseConnector();
            databaseConnector.connect();

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
                    alert.setContentText("Chủ hộ không có trong hộ khẩu");
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
