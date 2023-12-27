package com.example.cnpm;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.cnpm.DatabaseConnector.connection;

public class Menu{

    @FXML
    private Button close;

    @FXML
    private Button login_btn;

    @FXML
    private Button minimize;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @FXML
    private Label roleName;
    @FXML
    private TableColumn<Person, ?> MoneyCol;

    @FXML
    private RadioButton feeRButton;

    @FXML
    private RadioButton donateRButton;

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
    private AnchorPane moneyPane;

    @FXML
    private Button  peopleButton;

    @FXML
    private Button  homeButton;

    @FXML
    private Button  moneyButton;
    private double x = 0;
    private double y = 0;

    private List<Stage> newStages = new ArrayList(); //Lưu trữ tham chiếu các stage mới được mở thêm để sau logout đóng hết đi
    public void loginAdmin() throws SQLException, IOException {
        String sql = "SELECT * FROM account WHERE Name = ? and Password = ?";

        if(username.getText().isEmpty() || password.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Chưa điền thông tin");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng điền đầy đủ thông tin!");
            alert.showAndWait();
        } else{
            String VaiTro;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username.getText());
            preparedStatement.setString(2, password.getText());

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                VaiTro = resultSet.getString("VaiTro");
                System.out.println(VaiTro);
                alert.setTitle("Login Successfull");
                alert.setHeaderText(null);
                alert.setContentText("Đăng nhập thành công. Welcome " + VaiTro);
                alert.showAndWait();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
                Parent addPeopleRoot = loader.load();

                // Tạo một Stage mới
                Stage addPeopleStage = new Stage();
                addPeopleStage.setScene(new Scene(addPeopleRoot));


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

                Menu setRole = loader.getController();
                setRole.roleName.setText(VaiTro);
                if(VaiTro.equals("Kế toán")){
                    setRole.peopleButton.setDisable(true);
                    setRole.homeButton.setDisable(true);

                    setRole.peoplePane.setVisible(false);
                    setRole.homePane.setVisible(false);
                    setRole.moneyPane.setVisible(true);
                }else{
                    setRole.peoplePane.setVisible(true);
                    setRole.homePane.setVisible(false);
                    setRole.moneyPane.setVisible(false);

                }
                Stage currentStage = (Stage) login_btn.getScene().getWindow();
                currentStage.close();
                // Hiển thị Stage mới
                addPeopleStage.show();

            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText(null);
                alert.setContentText("Tài khoản hoặc mật khẩu không chính xác. Vui lòng nhập lại");
                alert.showAndWait();
            }
        }


    }
    @FXML
    void clickClose(ActionEvent event) {//nút close trên màn hình chính, nhấn để tắt TOÀN BỘ
        System.exit(0);
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.disconnect();

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
    public static String selectedId; // CCCD của nhân khẩu được chọn trong bảng Quản lý nhân khẩu
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
    public boolean checkNumberFormat (String s) {
        if(s == null) return false;
        for(int i=0;i<s.length();i++) {
            if(s.charAt(i) >= '0' && s.charAt(i) <= '9'){}
            else return false;
        }
        return true;
    }
    // Tìm kiếm nhân khẩu theo các tiêu chí đã chọn
    @FXML
    private void clickSearchPeople(ActionEvent event){ //nút search trên peoplePane
        //Cần kiểm tra mỗi trường được nhập rồi select trong csdl, sau đó hiển thị vào table bên dưới

        String queriedId = searchIDNameField.getText();
        String queriedName = searchIDNameField.getText();
        String queriedSex = seachGioiTinh.getValue();
        String tmp_queriedAgeFrom = ageFrom.getText();
        String tmp_queriedAgeTo = ageTo.getText();
        String queriedState = loaiLuuTru.getValue();
        LocalDate tmp_queriedFromDate = startDate.getValue();
        LocalDate tmp_queriedToDate = endDate.getValue();
        String queriedFromDate,queriedToDate;

        if(tmp_queriedFromDate != null && tmp_queriedToDate != null && tmp_queriedToDate.isBefore(tmp_queriedFromDate)){
            Alert alert;
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Ngày bắt đầu không được sau ngày kết thúc");
            alert.showAndWait();
        }
        else if(!checkNumberFormat(tmp_queriedAgeFrom) || !checkNumberFormat(tmp_queriedAgeTo)) {
            Alert alert;
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Độ tuổi phải là số nguyên >= 0");
            alert.showAndWait();
        }
        else {
            peopleTable.getItems().clear();
            ObservableList<Person> dataList = FXCollections.observableArrayList();

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

                    if(!NoiLuuTru.isEmpty() || (NoiLuuTru.isEmpty() && queriedFromDate.equals("1900/1/1") && queriedToDate.equals("2100/1/1") && queriedState.isEmpty())) {
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
        }
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

        if(!checkEmpty(HoTen) && NgaySinh != null && !checkEmpty(GioiTinh) && !checkEmpty(NoiSinh)
                && !checkEmpty(QuocTich) && !checkEmpty(QueQuan) && !checkEmpty(GioiTinh)) {
            DatabaseConnector databaseConnector = new DatabaseConnector();

            databaseConnector.updatePerson(SoCCCD,HoTen,BiDanh,NgaySinh.toString(),NoiSinh,GioiTinh,NgheNghiep,QueQuan,DanToc,QuocTich,NoiLamViec);

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
    @FXML
    void clickDeletePeople(MouseEvent event) {
        if(selectedId != null) {
            Alert alert;
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Bạn muốn xoá nhân khẩu " + selectedId + " ?");

            Optional<ButtonType> result = alert.showAndWait();
            if(result == null) {}
            else {
                if(result.get() == ButtonType.OK)
                {
                    DatabaseConnector databaseConnector = new DatabaseConnector();

                    if(databaseConnector.checkExistChuHoInHoKhauList(selectedId)) {
                        Alert alert1;
                        alert1 = new Alert(Alert.AlertType.INFORMATION);
                        alert1.setTitle("Error");
                        alert1.setHeaderText(null);
                        alert1.setContentText("Người này đang là chủ hộ của 1 hộ. Yêu cầu thay đổi chủ hộ hoặc xoá hộ đó nếu muốn xoá người này khỏi danh sách nhân khẩu!");
                        alert1.showAndWait();
                    }
                    else {
                        databaseConnector.deletePeople(selectedId);
                        Alert alert1;
                        alert1 = new Alert(Alert.AlertType.INFORMATION);
                        alert1.setTitle("Successful");
                        alert1.setHeaderText(null);
                        alert1.setContentText("Xoá thành công nhân khẩu!");
                        alert1.showAndWait();
                    }

                }
                else {}
            }
        }
    }
    @FXML
    private AnchorPane absentPane;

    @FXML
    private AnchorPane stayingPane;

    @FXML
    private Button registerAbsentPane;

    @FXML
    private Button registerStayingPane;

    @FXML
    private Button  openStayingPane;

    @FXML
    private Button  openAbsentPane;

    @FXML
    void clickRegisterHome(ActionEvent event) {//Nhấn register trong scene quản lý nhân khẩu để mở stage mới là registerHome.fxml
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("registerHome.fxml"));
            Parent addPeopleRoot = loader.load();

            // Tạo một Stage mới
            Stage addPeopleStage = new Stage();
            addPeopleStage.setScene(new Scene(addPeopleRoot));
            newStages.add(addPeopleStage);

            //Cài đặt để có thể di chuyển stage bằng kéo thả
            addPeopleRoot.setOnMousePressed((MouseEvent e) -> {
                x = e.getScreenX() - addPeopleStage.getX();
                y = e.getScreenY() - addPeopleStage.getY();
            });

            addPeopleRoot.setOnMouseDragged((MouseEvent e) -> {
                addPeopleStage.setX(e.getScreenX() - x);
                addPeopleStage.setY(e.getScreenY() - y);
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
    void openAbsentPane(ActionEvent event){//Nhấn tạm vắng để mở absentPane
        absentPane.setVisible(true);
        stayingPane.setVisible(false);
        registerAbsentPane.setVisible(true);
        registerStayingPane.setVisible(false);
    }
    @FXML
    void openStayingPane(ActionEvent event){//Nhấn tạm trú để mở stayingPane
        absentPane.setVisible(false);
        stayingPane.setVisible(true);
        registerAbsentPane.setVisible(false);
        registerStayingPane.setVisible(true);
    }

    @FXML
    private TextField registerAbsentCMNDField;

    @FXML
    private DatePicker registerAbsentEndDate;

    @FXML
    private DatePicker registerAbsentStartDate;

    @FXML
    void registerAbsent(ActionEvent event) {
        // Ấn vào nút đăng ký ở phần đăng ký tạm vắng sẽ cập nhật thông tin trong bang nhankhau_hokhau
        String SoCCCD = registerAbsentCMNDField.getText();
        LocalDate StartTime = registerAbsentStartDate.getValue();
        LocalDate EndTime = registerAbsentEndDate.getValue();

        if(!checkEmpty(SoCCCD) && StartTime != null && EndTime != null) {
            if(StartTime.isBefore(EndTime)) {
                DatabaseConnector databaseConnector = new DatabaseConnector();

                if (!databaseConnector.checkExistNhanKhau(SoCCCD)) {
                    Alert alert;
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Chưa tồn tại nhân khẩu, Đăng ký tạm vắng thất bại " + SoCCCD);
                    alert.showAndWait();
                } else {
                    if (databaseConnector.checkExistNhanKhauThuongTru(SoCCCD)) {
                        String Maho = databaseConnector.getCurrentIDHomeThuongTru(SoCCCD);
                        databaseConnector.updateTamVang(SoCCCD, Maho, StartTime.toString(), EndTime.toString());
                        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        Alert alert;
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Successful");
                        alert.setHeaderText(null);
                        alert.setContentText("Đăng ký tạm vắng thành công cho " + SoCCCD);
                        alert.showAndWait();
                        stage.close();
                    } else {
                        Alert alert;
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Người này đang không thường trú tại tổ dân phố ");
                        alert.showAndWait();
                    }
                }

            }
            else{
                Alert alert;
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Hãy nhập vào ngày bắt đầu trước ngày kết thúc");
                alert.showAndWait();
            }
            
        }
        else {
            Alert alert;
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Hãy điền đầy đủ dữ liệu");
            alert.showAndWait();
        }
    }

    @FXML
    private TextField registerStayingCMNDField;
    @FXML
    private TextField registerStayingDiaChiField;
    @FXML
    private TextField registerStayingHomeIDField;
    @FXML
    private DatePicker registerStayingEndDate;
    @FXML
    private DatePicker registerStayingStartDate;
    @FXML
    void registerStaying(ActionEvent event) {
        // Ấn vào nút đăng ký ở phần đăng ký tạm trú sẽ cập nhật thông tin trong bang nhankhau_hokhau
        String Maho = registerStayingHomeIDField.getText();
        String SoCCCD = registerStayingCMNDField.getText();
        String Diachi = registerStayingDiaChiField.getText();
        LocalDate StartTime = registerStayingStartDate.getValue();
        LocalDate EndTime = registerStayingEndDate.getValue();

        if(!checkEmpty(SoCCCD) && StartTime != null && EndTime != null && !checkEmpty(Diachi) && !checkEmpty(Maho)) {
            if (StartTime.isBefore(EndTime)) {
                DatabaseConnector databaseConnector = new DatabaseConnector();

                if (!databaseConnector.checkExistNhanKhau(SoCCCD)) {
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
                        addPeopleController.addPeopleCMNDField.setText(SoCCCD);
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
                } else {
                    if (!databaseConnector.checkExistNhanKhauThuongTru(SoCCCD) && !databaseConnector.checkExistNhanKhauTamTru(SoCCCD)) {
                        if (!databaseConnector.checkExistHoKhauInHoKhauList(Maho)) {
                            databaseConnector.insertNewHoKhau(Maho, SoCCCD, Diachi, "Tạm trú");
                            databaseConnector.insertnewTamTru(SoCCCD, Maho, StartTime.toString(), EndTime.toString(), Diachi);
                            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                            Alert alert;
                            alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Successful");
                            alert.setHeaderText(null);
                            alert.setContentText("Đăng ký tạm trú thành công cho " + SoCCCD);
                            alert.showAndWait();
                            stage.close();
                        } else {
                            Alert alert;
                            alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Mã hộ đã tồn tại");
                            alert.showAndWait();
                        }
                    } else {
                        Alert alert;
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Không thể đăng ký tạm trú cho người đã thường trú hoặc tạm trú trước đó !");
                        alert.showAndWait();
                    }
                }

            }
            else{
                Alert alert;
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Hãy nhập vào ngày bắt đầu trước ngày kết thúc");
                alert.showAndWait();
            }
        }

        else {
            Alert alert;
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Hãy điền đầy đủ dữ liệu");
            alert.showAndWait();
        }
    }


    /*
        Hết quản lý nhân khẩu
     */

    /*
        Quản lý hộ
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
    private TableColumn<Home,String> loaiSoColumn;
    // Tìm kiếm hộ theo tiêu chí đã chọn
    @FXML
    void clickSearchHome(ActionEvent event) {
        if(homeTable != null) homeTable.getItems().clear();
        ObservableList<Home> dataList = FXCollections.observableArrayList();

        String queriedIDNameHome = searchIDNameHome.getText();

        DatabaseConnector databaseConnector = new DatabaseConnector();

        ResultSet homeList = databaseConnector.getHomeList(queriedIDNameHome);

        try {
            while(homeList.next()) {
                String SoHK = homeList.getString(1);
                String TenChuHo = homeList.getString(3);
                String DiaChi = homeList.getString(4);
                String LoaiSo = homeList.getString(5);

                Home home = new Home(SoHK,TenChuHo,DiaChi,LoaiSo);
                dataList.add(home);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        idHoKhauColumn.setCellValueFactory(new PropertyValueFactory<Home,String>("SoHK"));
        tenChuHoColumn.setCellValueFactory(new PropertyValueFactory<Home,String>("TenChuHo"));
        diaChiHoKhauColumn.setCellValueFactory(new PropertyValueFactory<Home,String>("DiaChi"));
        loaiSoColumn.setCellValueFactory(new PropertyValueFactory<Home,String>("LoaiSo"));
        homeTable.setItems(dataList);

    }
    public static String selectedIDHome; // hộ được chọn
    public static String selectedLoaiSo; // Loại sổ của hộ được chọn
    @FXML
    void selectHome(MouseEvent event) {
        selectedIDHome = homeTable.getSelectionModel().getSelectedItem().getSoHK();
        selectedLoaiSo = homeTable.getSelectionModel().getSelectedItem().getLoaiSo();
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

                databaseConnector.deleteMemberFromTempHoKhauTable(id);

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
    // Bấm done để hoàn thành thêm hộ mới
    @FXML
    void finishAddNewHome(MouseEvent event) {
        String HoKhauID = idHKFieldAddHome.getText();
        String ChuHoID = idChuHoFieldAddHome.getText();
        String DiaChi = addressHomeFieldAddHome.getText();
        String LoaiSo = loaiSoFieldAddHome.getValue();
        // Các trường mã hộ, số cccd của chủ hộ, địa chỉ không được để trống
        if(!HoKhauID.isEmpty() && !ChuHoID.isEmpty() && !DiaChi.isEmpty() && LoaiSo != null) {
            DatabaseConnector databaseConnector = new DatabaseConnector();

            // Nếu chủ hộ đã có trong danh sách nhân khẩu của hộ và không là chủ hộ của hộ khác và mã hộ chưa tồn tại
            // thì tạo hộ mới với những người trong danh sách tạm
            if(!databaseConnector.checkExistChuHoInHoKhauList(ChuHoID) && !databaseConnector.checkExistHoKhauInHoKhauList(HoKhauID)
                    && databaseConnector.checkExistChuHoInTempHoKhauTable(ChuHoID))
            {
                databaseConnector.insertNewHoKhau(HoKhauID,ChuHoID,DiaChi,LoaiSo);
                ResultSet peopleListInsertIntoNewHoKhau = databaseConnector.getTempHoKhauTable();
                // Nếu đang tạo một sổ tạm trú
                if(LoaiSo.equals("Tạm trú")) {
                    try{
                        while(peopleListInsertIntoNewHoKhau.next()) {
                            String NhanKhauID = peopleListInsertIntoNewHoKhau.getString("CCCD");
                            String QuanHeChuHo = peopleListInsertIntoNewHoKhau.getString("QuanHeChuHo");
                            // Nếu thêm 1 người đang thường trú ở 1 hộ thì cho tiếp tục tạm vắng ở hộ đó
                            if(databaseConnector.checkExistNhanKhauThuongTru(NhanKhauID)) {
                                // Nếu chưa đki tạm vắng thì phải đki tạm vắng ở hộ thường trú hiện tại
                                if(databaseConnector.checkNhanKhauTamVang(NhanKhauID)){}
                                else {
                                    String currentIDHomeThuongTru = databaseConnector.getCurrentIDHomeThuongTru(NhanKhauID);
                                    databaseConnector.insertNewNhanKhauHoKhau(NhanKhauID,currentIDHomeThuongTru,(new Date(System.currentTimeMillis())).toString(),
                                            "2100-1-1","Tạm vắng",QuanHeChuHo);
                                }
                            }
                            // Nếu thêm 1 người đang tạm trú ở 1 hộ thì kết thúc bản ghi tạm trú ở hộ đó
                            if(databaseConnector.checkExistNhanKhauTamTru(NhanKhauID)) {
                                databaseConnector.updateNgayKetThucTamTru(NhanKhauID);
                            }
                            databaseConnector.insertNewNhanKhauHoKhau(NhanKhauID,HoKhauID,(new Date(System.currentTimeMillis())).toString(),
                                    "2100-1-1",LoaiSo,QuanHeChuHo);
                        }
                        Alert alert;
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Successful");
                        alert.setHeaderText(null);
                        alert.setContentText("Tạo thành công hộ mới");
                        alert.showAndWait();
                        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        newStages.remove(stage);
                        stage.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                // Nếu tạo một hộ thường trú
                else {
                    try{
                        while(peopleListInsertIntoNewHoKhau.next()) {
                            String NhanKhauID = peopleListInsertIntoNewHoKhau.getString("CCCD");
                            String QuanHeChuHo = peopleListInsertIntoNewHoKhau.getString("QuanHeChuHo");
                            // Nếu thêm 1 người đang thường trú ở 1 hộ thì kết thúc bản ghi tạm vắng và thường trú ở hộ đó
                            if(databaseConnector.checkExistNhanKhauThuongTru(NhanKhauID)) {
                                databaseConnector.updateNgayKetThucThuongTru(NhanKhauID);
                                databaseConnector.updateNgayKetThucTamVang(NhanKhauID);
                            }
                            // Nếu thêm 1 người đang tạm trú ở 1 hộ thì vẫn giữ bản ghi tạm trú ở hộ này và thêm bản ghi tạm vắng ở hộ mới
                            if(databaseConnector.checkExistNhanKhauTamTru(NhanKhauID)) {
                                databaseConnector.insertNewNhanKhauHoKhau(NhanKhauID,HoKhauID,(new Date(System.currentTimeMillis())).toString(),
                                        "2100-1-1","Tạm vắng",QuanHeChuHo);
                            }
                            databaseConnector.insertNewNhanKhauHoKhau(NhanKhauID,HoKhauID,(new Date(System.currentTimeMillis())).toString(),
                                    "2100-1-1",LoaiSo,QuanHeChuHo);
                        }

                        Alert alert;
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Successful");
                        alert.setHeaderText(null);
                        alert.setContentText("Tạo thành công hộ mới");
                        alert.showAndWait();
                        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        newStages.remove(stage);
                        stage.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
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
                    alert.setContentText("Đã tồn tại số hộ này ");
                    alert.showAndWait();
                }
                if(!databaseConnector.checkExistChuHoInTempHoKhauTable(ChuHoID)) {
                    Alert alert;
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Chủ hộ chưa được thêm vào hộ này");
                    alert.showAndWait();
                }

            }


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

    // Cập nhật thông tin của hộ đã có
    @FXML
    void clickDetailHouseHold(ActionEvent event) {//Nhấn detail trong scene quản lý hộ để mở stage mới là updateHome.fxml
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

                databaseConnector.deleteTempHoKhauTable();

                ResultSet homeInfo = databaseConnector.getHomeInfo(selectedIDHome);
                while(homeInfo.next()) {
                    updateHomeController.idChuHoFieldUpdateHome.setText(homeInfo.getString(2));
                    updateHomeController.addressHomeFieldUpdateHome.setText(homeInfo.getString(3));
                    updateHomeController.loaiSoFieldUpdateHome.setText(homeInfo.getString(4));
                    updateHomeController.loaiSoFieldUpdateHome.setEditable(false);
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
    private TextField loaiSoFieldUpdateHome;
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

                databaseConnector.deleteMemberFromTempHoKhauTable(id);

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
        String LoaiSo = loaiSoFieldUpdateHome.getText();
        if(!ChuHoID.isEmpty() && !DiaChi.isEmpty()) {
            DatabaseConnector databaseConnector = new DatabaseConnector();

            // Nếu chủ hộ không nằm trong danh sách nhân khẩu mới
            if(!databaseConnector.checkExistChuHoInTempHoKhauTable(ChuHoID)) {
                Alert alert;
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Chưa có chủ hộ trong danh sách của hộ");
                alert.showAndWait();
            }
            else {
                databaseConnector.updateOwnerAddressHome(HoKhauID,ChuHoID,DiaChi); // Cập nhật thông tin chủ hộ & dịa chỉ
                ResultSet newMemberList = databaseConnector.getTempHoKhauTable(); // danh sách nhân khẩu mới của hộ
                ResultSet currentMemberList = databaseConnector.getCurrentMember(HoKhauID);
                // Nếu hộ được cập nhật là 1 hộ thường trú
                if(LoaiSo.equals("Thường trú")) {
                    try {
                        // Những người không còn trong danh sách sẽ bị kết thúc bản ghi thường trú (và tạm vắng nếu có)
                        while(currentMemberList.next()) {
                            String NhanKhauID = currentMemberList.getString(1);
                            if(!databaseConnector.checkExistNhanKhauTempHoKhauTable(NhanKhauID)){
                                databaseConnector.updateNgayKetThucThuongTru(NhanKhauID);
                                databaseConnector.updateNgayKetThucTamVang(NhanKhauID);
                            }
                        }
                        // Đối với những người trong danh sách mới, nếu có trong danh sách cũ thì chỉ update quan hệ chủ hộ
                        while(newMemberList.next()) {
                            String idNewMember = newMemberList.getString("CCCD");
                            String relationNewMember = newMemberList.getString("QuanHeChuHo");
                            // Nếu người được thêm đã có trong hộ từ trước thì chỉ update quan hệ chủ hộ
                            if(databaseConnector.checkExistNhanKhauHoKhau(idNewMember,HoKhauID))
                                databaseConnector.updateRelation(idNewMember,HoKhauID,relationNewMember);
                            // Nếu không có trong danh sách cũ
                            else {
                                // Nếu người được thêm đang thường trú ở nơi khác và thì kết thúc thường trú ở đó (và tạm vắng nếu có)
                                if(databaseConnector.checkExistNhanKhauThuongTru(idNewMember)) {
                                    databaseConnector.updateNgayKetThucThuongTru(idNewMember);
                                    databaseConnector.updateNgayKetThucTamVang(idNewMember);
                                }
                                // Nếu người được thêm đang tạm trú ở nơi khác thì tiếp tục tạm trú và tiếp tục cho tạm vắng ở hộ mới
                                if(databaseConnector.checkExistNhanKhauTamTru(idNewMember)) {
                                    databaseConnector.insertNewNhanKhauHoKhau(idNewMember,HoKhauID,(new Date(System.currentTimeMillis())).toString(),
                                            "2100-01-01","Tạm vắng",relationNewMember);
                                }
                                // Thêm bản ghi về cư trú của người được thêm ở hộ đang sửa vào bảng nhankhau_hokhau
                                databaseConnector.insertNewNhanKhauHoKhau(idNewMember,HoKhauID,(new Date(System.currentTimeMillis())).toString(),
                                        "2100-01-01",LoaiSo,relationNewMember);
                            }
                        }
                        Alert alert;
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Successful");
                        alert.setHeaderText(null);
                        alert.setContentText("Cập nhật hộ " + HoKhauID + " thành công!");
                        alert.showAndWait();
                        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        newStages.remove(stage);
                        stage.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                // Nếu đang cập nhật một hộ tạm trú
                else {
                    try {
                        // Đối với những người trong danh sách cũ không còn ở trong danh sách mới thì cho kết thúc tạm trú (và cả tạm vắng nếu có)
                        while(currentMemberList.next()) {
                            String NhanKhauID = currentMemberList.getString(1);
                            if(!databaseConnector.checkExistNhanKhauTempHoKhauTable(NhanKhauID)){
                                databaseConnector.updateNgayKetThucTamTru(NhanKhauID);
                                databaseConnector.updateNgayKetThucTamVang(NhanKhauID);
                            }
                        }
                        // Xét danh sách mới, thành viên có trong danh sách cũ thì chỉ cập nhật quan hệ chủ hộ
                        while(newMemberList.next()) {
                            String idNewMember = newMemberList.getString("CCCD");
                            String relationNewMember = newMemberList.getString("QuanHeChuHo");
                            // Nếu người được thêm đã có trong hộ từ trước thì chỉ update quan hệ chủ hộ
                            if(databaseConnector.checkExistNhanKhauHoKhau(idNewMember,HoKhauID))
                                databaseConnector.updateRelation(idNewMember,HoKhauID,relationNewMember);
                            else {
                                // Nếu người được thêm đang thường trú ở nơi khác thì tiếp tục thường trú ở đó nhưng phải đki tạm vắng
                                if(databaseConnector.checkExistNhanKhauThuongTru(idNewMember)) {
                                    // Nếu đang tạm vắng r thì tiếp tục tạm vắng, nếu chưa có thì đăng ký tạm vắng mới
                                    if(databaseConnector.checkNhanKhauTamVang(idNewMember)) {}
                                    else {
                                        databaseConnector.insertNewNhanKhauHoKhau(idNewMember,HoKhauID,(new Date(System.currentTimeMillis())).toString(),
                                                "2100-01-01","Tạm vắng",relationNewMember);
                                    }
                                }
                                // Nếu người được thêm đang tạm trú ở nơi khác thì kết thúc tạm trú ở đó
                                if(databaseConnector.checkExistNhanKhauTamTru(idNewMember)) {
                                    databaseConnector.updateNgayKetThucTamTru(idNewMember);
                                }
                                // Thêm bản ghi về cư trú của người được thêm ở hộ đang sửa vào bảng nhankhau_hokhau
                                databaseConnector.insertNewNhanKhauHoKhau(idNewMember,HoKhauID,(new Date(System.currentTimeMillis())).toString(),
                                        "2100-01-01",LoaiSo,relationNewMember);
                            }

                        }
                        Alert alert;
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Successful");
                        alert.setHeaderText(null);
                        alert.setContentText("Cập nhật hộ " + HoKhauID + " thành công!");
                        alert.showAndWait();
                        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        newStages.remove(stage);
                        stage.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }



            }



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
    private ChoiceBox<String> loaiSoFieldAddHome;
    @FXML
    void clickAddHouseHold(ActionEvent event) {//Nhấn add trong scene quản lý hộ để mở stage mới là addHome.fxml
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addHome.fxml"));
            Parent addHomeRoot = loader.load();

            // Tạo một Stage mới
            Stage addHomeStage = new Stage();
            addHomeStage.setScene(new Scene(addHomeRoot));
            newStages.add(addHomeStage);

            Menu addHomeController = loader.getController();
            addHomeController.loaiSoFieldAddHome.setItems(FXCollections.observableArrayList("Thường trú","Tạm trú"));

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

            databaseConnector.deleteTempHoKhauTable();

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
    private TextField loaiSoNewSplitHome;

    @FXML
    private TextField loaiSoSplitHome;

    @FXML
    private TableColumn<PeopleInfoOfHome, String> idSplitMember;

    @FXML
    private TableColumn<PeopleInfoOfHome, String> nameSplitMember;

    @FXML
    private TableColumn<PeopleInfoOfHome, String> relationSplitMember;

    void refreshTempSplitHomeTable(Menu controller){
        DatabaseConnector databaseConnector = new DatabaseConnector();

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

    }

    @FXML
    private TableView<PeopleInfoOfHome> tempSplitHomeTable;
    // Mở cửa sổ tách hộ, thêm danh sách các thành viên hiện tại vào phần thành viên
    @FXML
    void clickSplitHouseHold(ActionEvent event) {//Nhấn split trong scene quản lý hộ để mở stage mới là splitHome.fxml
        if(selectedIDHome != null && selectedLoaiSo.equals("Thường trú")) { // Chỉ tách sổ hộ chứ không tách sổ tạm trú
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("splitHome.fxml"));
                Parent splitHomeRoot = loader.load();

                // Tạo một Stage mới
                Stage splitHomeStage = new Stage();
                splitHomeStage.setScene(new Scene(splitHomeRoot));
                newStages.add(splitHomeStage);

                DatabaseConnector databaseConnector = new DatabaseConnector();

                databaseConnector.deleteTempHoKhauTable();

                Menu splitHomeController = loader.getController();
                ResultSet homeInfo = databaseConnector.getHomeInfo(selectedIDHome);
                // setText cho các trường mã hộ, cccd chủ hộ và địa chỉ của hộ cha và setText cho địa chỉ hộ tách ra là địa chỉ của hộ cha
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
                    }
                    splitHomeController.loaiSoSplitHome.setText("Thường trú");
                    splitHomeController.loaiSoNewSplitHome.setText("Thường trú");
                    splitHomeController.loaiSoNewSplitHome.setEditable(false);
                    splitHomeController.loaiSoSplitHome.setEditable(false);
                } catch (SQLException e) {
                    e.printStackTrace();
                }


                DatabaseConnector databaseConnector1 = new DatabaseConnector();

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

                            databaseConnector2.deleteMemberFromTempHoKhauTable(id);

                            refreshTempSplitHomeTable(splitHomeController);
                        });

                        hbox.getChildren().addAll(cmnd, cmndText, qh, qhText, addButton, delButton);
                        hbox.setPadding(insets);
                        splitHomeController.container.getChildren().add(hbox);
                    }
                } catch(SQLException e) {
                    e.printStackTrace();
                }

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
    // Ấn hoàn thành tách hộ
    @FXML
    void finishSplitHome(MouseEvent event) {
        String HoKhauID = idNewHKFieldSplitHome.getText();
        String ChuHoID = idNewChuHoFieldSplitHome.getText();
        String DiaChi = addressNewHomeSplitHome.getText();
        String LoaiSo = loaiSoNewSplitHome.getText();
        // Nếu đã nhập đủ dữ liệu của hộ đc tách ra
        if(!HoKhauID.isEmpty() && !ChuHoID.isEmpty()) {
            DatabaseConnector databaseConnector = new DatabaseConnector();

            // Mã của hộ mới không được đã tồn tại và chủ hộ của hộ mới phải được có trong danh sách tạm
            if(!databaseConnector.checkExistHoKhauInHoKhauList(HoKhauID) && databaseConnector.checkExistChuHoInTempHoKhauTable(ChuHoID))
            {
                databaseConnector.insertNewHoKhau(HoKhauID,ChuHoID,DiaChi,LoaiSo);
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
                    alert.setContentText("Tách thành công hộ mới");
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
                    alert.setContentText("Đã tồn tại số hộ này ");
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
    void clickDeleteHome(ActionEvent event) {
        if(selectedIDHome != null) {
            Alert alert;
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Bạn muốn xoá hộ khẩu " + selectedIDHome + " ? Các thành viên cũng sẽ bị xoá thông tin đăng ký lưu trú");

            Optional<ButtonType> result = alert.showAndWait();
            if(result == null) {}
            else {
                if(result.get() == ButtonType.OK)
                {
                    DatabaseConnector databaseConnector = new DatabaseConnector();

                    databaseConnector.deleteHome(selectedIDHome);

                    Alert alert1;
                    alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Successful");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Xoá thành công hộ khẩu " + selectedIDHome + "!");
                    alert1.showAndWait();
                }
                else {}
            }
        }
    }
    @FXML
    private TextField addressFieldChangeLogHome;

    @FXML
    private TableView<LichSuThayDoi> changeLogHomeTable;

    @FXML
    private TableColumn<LichSuThayDoi, String> contentChangeLogColumn;

    @FXML
    private TableColumn<LichSuThayDoi, String> dateChangeLogColumn;

    @FXML
    private TextField idChuHoFieldChangeLogHome;

    @FXML
    private TextField idHKFieldLogHome;

    @FXML
    private TextField loaiSoFieldChangeLogHome;

    @FXML
    void clickChangeLogHome (MouseEvent event) {
        if(selectedIDHome != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("changeLogHome.fxml"));
                Parent changeLogHomeRoot = loader.load();

                // Tạo một Stage mới
                Stage changeLogHomeStage = new Stage();
                changeLogHomeStage.setScene(new Scene(changeLogHomeRoot));
                newStages.add(changeLogHomeStage);

                Menu changeLogHomeController = loader.getController();
                DatabaseConnector databaseConnector = new DatabaseConnector();

                ResultSet homeInfo = databaseConnector.getHomeInfo(selectedIDHome);
                try {
                    while(homeInfo.next()) {
                        changeLogHomeController.idHKFieldLogHome.setText(selectedIDHome);
                        changeLogHomeController.loaiSoFieldChangeLogHome.setText(homeInfo.getString("LoaiSo"));
                        changeLogHomeController.idChuHoFieldChangeLogHome.setText(homeInfo.getString("ChuHoID"));
                        changeLogHomeController.addressFieldChangeLogHome.setText(homeInfo.getString("DiaChi"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                ObservableList<LichSuThayDoi> dataList = FXCollections.observableArrayList();
                ResultSet changeLogList = databaseConnector.getChangeLog(selectedIDHome);
                try {
                    while(changeLogList.next()) {
                        String NgayThayDoi = changeLogList.getString("NgayThayDoi");
                        String NoiDung = changeLogList.getString("NoiDung");
                        LichSuThayDoi lichSuThayDoi = new LichSuThayDoi(NgayThayDoi,NoiDung);
                        dataList.add(lichSuThayDoi);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                changeLogHomeController.dateChangeLogColumn.setCellValueFactory(new PropertyValueFactory<LichSuThayDoi,String>("NgayThayDoi"));
                changeLogHomeController.contentChangeLogColumn.setCellValueFactory(new PropertyValueFactory<LichSuThayDoi,String>("NoiDung"));

                changeLogHomeController.changeLogHomeTable.setItems(dataList);

                //Cài đặt để có thể di chuyển stage bằng kéo thả
                changeLogHomeRoot.setOnMousePressed((MouseEvent e) -> {
                    x = e.getScreenX() - changeLogHomeStage.getX();
                    y = e.getScreenY() - changeLogHomeStage.getY();
                });

                changeLogHomeRoot.setOnMouseDragged((MouseEvent e) -> {
                    changeLogHomeStage.setX(e.getScreenX() - x);
                    changeLogHomeStage.setY(e.getScreenY() - y);
                });

                // Đặt kiểu modality của Stage mới là NONE
                changeLogHomeStage.initModality(Modality.NONE);
                changeLogHomeStage.initStyle(StageStyle.TRANSPARENT);

                // Hiển thị Stage mới
                changeLogHomeStage.show();

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

    }




    /*
        Hết quản lý hộ
     */

    /*
        Quản lý thu chi
     */
    @FXML
    private TableView<KhoanPhiInfo> moneyTable;
    @FXML
    private TableColumn<KhoanPhiInfo, String> dateMoneyCol;
    @FXML
    private TableColumn<KhoanPhiInfo, Integer> totalMoneyCol;
    @FXML
    private TableColumn<KhoanPhiInfo, String> idMoneyCol;
    @FXML
    private TableColumn<KhoanPhiInfo, String> nameMoneyCol;
    @FXML
    private TableColumn<KhoanPhiInfo, String> typeMoneyCol;
    @FXML
    private TextField searchIDNameKhoanPhiField;

    public static String selectedIDKhoanPhi; // Lưu id khoản phí được chọn
    @FXML
    void selectKhoanPhi(MouseEvent event) {
        selectedIDKhoanPhi = moneyTable.getSelectionModel().getSelectedItem().getID();
    }
    // Tra cứu khoản phí
    @FXML
    void clickSearchKhoanPhi(MouseEvent event) {
        if(moneyTable != null) moneyTable.getItems().clear();
        ObservableList<KhoanPhiInfo> dataList = FXCollections.observableArrayList();

        String queriedIDNameKhoanPhi = searchIDNameKhoanPhiField.getText();
        String queriedTypeKhoanPhi = "";
        if(donateRButton.isSelected()) {
            queriedTypeKhoanPhi = "Đóng góp";
        }
        else if(feeRButton.isSelected()) {
            queriedTypeKhoanPhi = "Phí thu";
        }

        DatabaseConnector databaseConnector = new DatabaseConnector();

        ResultSet khoanPhiList = databaseConnector.getKhoanPhiList(queriedIDNameKhoanPhi,queriedTypeKhoanPhi);

        try {
            while(khoanPhiList.next()) {
                String ID = khoanPhiList.getString("ID");
                String TenPhi = khoanPhiList.getString("TenPhi");
                String NgayBatDauThu = khoanPhiList.getString("NgayBatDauThu");
                String LoaiKhoanPhi = khoanPhiList.getString("Loai");
                Integer TongTienDaThu = databaseConnector.getTongTienDaDongChoKhoanPhi(ID);

                KhoanPhiInfo khoanPhiInfo = new KhoanPhiInfo(ID,TenPhi,TongTienDaThu,NgayBatDauThu,LoaiKhoanPhi);
                dataList.add(khoanPhiInfo);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        idMoneyCol.setCellValueFactory(new PropertyValueFactory<KhoanPhiInfo,String>("ID"));
        nameMoneyCol.setCellValueFactory(new PropertyValueFactory<KhoanPhiInfo,String>("TenKhoanPhi"));
        totalMoneyCol.setCellValueFactory(new PropertyValueFactory<KhoanPhiInfo,Integer>("TongTienDaThu"));
        dateMoneyCol.setCellValueFactory(new PropertyValueFactory<KhoanPhiInfo,String>("NgayBatDauThu"));
        typeMoneyCol.setCellValueFactory(new PropertyValueFactory<KhoanPhiInfo,String>("LoaiKhoanPhi"));
        moneyTable.setItems(dataList);

    }
    // Nhấn mớ giao diện đóng phí
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
    // Nhấn mở giao diện thống kê thu phí
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

    @FXML
    private void openMoneyPane(ActionEvent event) {
        peoplePane.setVisible(false);
        homePane.setVisible(false);
        moneyPane.setVisible(true);
    }
    // Nhấn chọn thu phí
    public void clickFeeRButton(ActionEvent actionEvent) {
        if(donateRButton.isSelected()) {
            donateRButton.setSelected(false);
            feeRButton.setSelected(true);
        }
    }
    // Nhấn chọn đóng góp
    public void clickDonateRButton(ActionEvent actionEvent) {
        if(feeRButton.isSelected()) {
            feeRButton.setSelected(false);
            donateRButton.setSelected(true);
        }
    }

    @FXML
    private TableColumn<DongPhiLog, String> addressColumnAnalyzeMoney;
    @FXML
    private TableView<DongPhiLog> analyzeMoneyTable;
    @FXML
    private TableColumn<DongPhiLog, Integer> conThieuColumnAnalyzeMoney;
    @FXML
    private TableColumn<DongPhiLog, Integer> daDongColumnAnalyzeMoney;
    @FXML
    private DatePicker fromDateDongPhi;
    @FXML
    private TextField idHoAnalyzeMoney;
    @FXML
    private TableColumn<DongPhiLog, String> idHoColumnAnalyzeMoney;
    @FXML
    private ComboBox<String> loaiPhiAnalyzeMoney;
    @FXML
    private TableColumn<DongPhiLog, String> ngayDongColumnAnalyzeMoney;
    @FXML
    private TableColumn<DongPhiLog, Integer> soTienDongColumnAnalyzeMoney;
    @FXML
    private ComboBox<String> tenKhoanPhiAnalyzeMoney;
    @FXML
    private TableColumn<DongPhiLog, String> tenKhoanPhiColumnAnalyzeMoney;
    @FXML
    private DatePicker toDateDongPhi;
    // Ấn vào combox Loại phí thì hiện ra 3 option Tất cả, Phí thu, Đóng góp
    @FXML
    void actionOnClickLoaiPhiAnalyzeMoney(MouseEvent event) {
        loaiPhiAnalyzeMoney.setItems(FXCollections.observableArrayList("Tất cả","Phí thu","Đóng góp"));
        tenKhoanPhiAnalyzeMoney.getItems().clear();
        tenKhoanPhiAnalyzeMoney.setValue("");
    }
    // Ấn vào khoản phí thì hiện ra các option là tên các khoản phí ứng với khoản phí đã chọn
    @FXML
    void actionOnClickKhoanPhiAnalyzeMoney(MouseEvent event) {
        ObservableList<String> tenKhoanPhiList = FXCollections.observableArrayList();
        DatabaseConnector databaseConnector = new DatabaseConnector();

        String type = loaiPhiAnalyzeMoney.getValue();
        // Nếu chọn tất cả các loại phí thì thêm option chọn tất cả các khoản phí
        if(type == null || type.equals("Tất cả")) {
            type = "";
            tenKhoanPhiList.add("Tất cả");
        }
        ResultSet resultSet = databaseConnector.getTenKhoanPhiList(type);
        try {
            while(resultSet.next()) {
                tenKhoanPhiList.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tenKhoanPhiAnalyzeMoney.setItems(tenKhoanPhiList);
    }
    // Nhấn tìm kiếm lịch sử thu phí, đóng góp
    @FXML
    void clickSearchDongPhiLog(MouseEvent event) {
        if(analyzeMoneyTable != null) analyzeMoneyTable.getItems().clear();
        ObservableList<DongPhiLog> dataList = FXCollections.observableArrayList();

        String queriedHoKhauID = idHoAnalyzeMoney.getText();
        String queriedLoaiPhi;
        if(loaiPhiAnalyzeMoney.getValue() == null) queriedLoaiPhi = "";
        else if(loaiPhiAnalyzeMoney.getValue().equals("Tất cả")) queriedLoaiPhi = "";
        else queriedLoaiPhi = loaiPhiAnalyzeMoney.getValue();

        String queriedTenKhoanPhi;
        if(tenKhoanPhiAnalyzeMoney.getValue() == null) queriedTenKhoanPhi = "";
        else if(tenKhoanPhiAnalyzeMoney.getValue().equals("Tất cả")) queriedTenKhoanPhi = "";
        else queriedTenKhoanPhi = tenKhoanPhiAnalyzeMoney.getValue();

        String queriedFromDate;
        if(fromDateDongPhi.getValue() == null) queriedFromDate = "1900-01-01";
        else queriedFromDate = fromDateDongPhi.getValue().toString();

        String queriedToDate;
        if(toDateDongPhi.getValue() == null) queriedToDate = "2100-01-01";
        else queriedToDate = toDateDongPhi.getValue().toString();

        DatabaseConnector databaseConnector = new DatabaseConnector();

        ResultSet dongPhiLogList = databaseConnector.getLichSuDongPhi(queriedHoKhauID,queriedLoaiPhi,queriedTenKhoanPhi,queriedFromDate,queriedToDate);

        try {
            while(dongPhiLogList.next()) {
                String HoKhauID = dongPhiLogList.getString(1);
                String DiaChi = dongPhiLogList.getString(2);
                String TenKhoanPhi = dongPhiLogList.getString(3);
                String NgayDong = dongPhiLogList.getDate(4).toString();
                Integer SoTien = dongPhiLogList.getInt(5);

                Integer DaDong = databaseConnector.getTongTienDaDong(HoKhauID,TenKhoanPhi,NgayDong);
                Integer PhaiDong = databaseConnector.getSoTienPhaiDong(HoKhauID,TenKhoanPhi);
                Integer ConThieu;
                if(PhaiDong == 0) ConThieu = 0;
                else ConThieu = PhaiDong - DaDong;
                DongPhiLog dongPhiLog = new DongPhiLog(HoKhauID,DiaChi,TenKhoanPhi,NgayDong,SoTien,DaDong,ConThieu);
                dataList.add(dongPhiLog);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        idHoColumnAnalyzeMoney.setCellValueFactory(new PropertyValueFactory<DongPhiLog,String>("HoKhauID"));
        addressColumnAnalyzeMoney.setCellValueFactory(new PropertyValueFactory<DongPhiLog,String>("DiaChi"));
        tenKhoanPhiColumnAnalyzeMoney.setCellValueFactory(new PropertyValueFactory<DongPhiLog,String>("KhoanPhi"));
        ngayDongColumnAnalyzeMoney.setCellValueFactory(new PropertyValueFactory<DongPhiLog,String>("NgayDong"));
        soTienDongColumnAnalyzeMoney.setCellValueFactory(new PropertyValueFactory<DongPhiLog,Integer>("SoTienDong"));
        daDongColumnAnalyzeMoney.setCellValueFactory(new PropertyValueFactory<DongPhiLog,Integer>("DaDong"));
        conThieuColumnAnalyzeMoney.setCellValueFactory(new PropertyValueFactory<DongPhiLog,Integer>("ConThieu"));

        analyzeMoneyTable.setItems(dataList);

    }

    @FXML
    private TextField diaChiHoDongPhi;
    @FXML
    private TextField idHoDongPhi;
    @FXML
    private ComboBox<String> loaiPhiAddMoney;
    @FXML
    private DatePicker ngayDongPhi;
    @FXML
    private Label soTienConThieu;
    @FXML
    private TextField soTienNop;
    @FXML
    private TextField tenChuHoDongPhi;
    @FXML
    private ComboBox<String> tenKhoanPhiAddMoney;
    // Nhấn chọn loại phí trong phần đóng phí
    @FXML
    void actionOnClickLoaiPhiAddMoney(MouseEvent event) {
        loaiPhiAddMoney.setItems(FXCollections.observableArrayList("Phí thu","Đóng góp"));
        tenKhoanPhiAddMoney.getItems().clear();
        tenKhoanPhiAddMoney.setValue("");
    }
    // Nhấn chọn khoản phí trong phần đóng phí
    @FXML
    void actionOnClickKhoanPhiAddMoney(MouseEvent event) {
        ObservableList<String> tenKhoanPhiList = FXCollections.observableArrayList();
        DatabaseConnector databaseConnector = new DatabaseConnector();

        String type = loaiPhiAddMoney.getValue();
        // Nếu chọn tất cả các loại phí thì thêm option chọn tất cả các khoản phí
        if(type == null) type = "";
        ResultSet resultSet = databaseConnector.getTenKhoanPhiList(type);
        try {
            while(resultSet.next()) {
                tenKhoanPhiList.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tenKhoanPhiAddMoney.setItems(tenKhoanPhiList);

    }
    // Cập nhật số tiền còn thiếu ứng với các thông tin đã nhập
    // khi chọn khoản phí
    @FXML
    void chooseKhoanPhi(ActionEvent event) {
        String TenKhoanPhi = tenKhoanPhiAddMoney.getValue();
        if(TenKhoanPhi != null) {
            DatabaseConnector databaseConnector = new DatabaseConnector();

            soTienConThieu.setText(databaseConnector.getSoTienConThieu(idHoDongPhi.getText(),TenKhoanPhi) + " Đ");

        }
    }
    // khi thay đổi id của hộ
    @FXML
    void getTypedHoKhauIDAddMoney(KeyEvent event) {
        String HoKhauID = idHoDongPhi.getText();
        DatabaseConnector databaseConnector = new DatabaseConnector();


        ResultSet resultSet = databaseConnector.getHomeInfo(HoKhauID);
        try{
            String TenChuHo = "",DiaChi = "";
            while(resultSet.next()) {
                String ChuHoID = resultSet.getString("ChuHoID");
                TenChuHo = databaseConnector.getHoTen(ChuHoID);
                DiaChi = resultSet.getString("DiaChi");
            }
            tenChuHoDongPhi.setText(TenChuHo);
            diaChiHoDongPhi.setText(DiaChi);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(tenKhoanPhiAddMoney.getValue() != null) {
            soTienConThieu.setText(databaseConnector.getSoTienConThieu(HoKhauID,tenKhoanPhiAddMoney.getValue()) + " Đ");
        }


    }
    // Huỷ đóng phí
    @FXML
    void cancelDongPhi(MouseEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        newStages.remove(stage);
        stage.close();
    }
    // Hoàn thành đóng phí
    @FXML
    void finishDongPhi(MouseEvent event) {
        if(idHoDongPhi.getText().isEmpty() || tenKhoanPhiAddMoney.getValue() == null || ngayDongPhi.getValue() == null || soTienNop.getText().isEmpty()) {
            Alert alert;
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Nhập thiếu dữ liệu");
            alert.showAndWait();
        }
        else {
            String HoKhauID = idHoDongPhi.getText();
            int IDPhi;
            String NgayDong = ngayDongPhi.getValue().toString();
            int SoTienNop = Integer.parseInt(soTienNop.getText());
            DatabaseConnector databaseConnector = new DatabaseConnector();

            IDPhi = databaseConnector.getIDPhi(tenKhoanPhiAddMoney.getValue());
            if(databaseConnector.checkExistLogDongPhi(HoKhauID,IDPhi,NgayDong)) databaseConnector.updateDongPhiLog(HoKhauID,IDPhi,NgayDong,SoTienNop);
            else databaseConnector.insertNewDongPhiLog(HoKhauID,IDPhi,NgayDong,SoTienNop);

            Alert alert;
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful");
            alert.setHeaderText(null);
            alert.setContentText("Đóng phí thành công");
            alert.showAndWait();
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            newStages.remove(stage);
            stage.close();

        }
    }
    // Nhấn mở của sổ tạo khoản phí mới
    @FXML
    void clickAddFee(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addFee.fxml"));
            Parent addFeeRoot = loader.load();

            // Tạo một Stage mới
            Stage addFeeStage = new Stage();
            addFeeStage.setScene(new Scene(addFeeRoot));
            newStages.add(addFeeStage);

            Menu addFeeController = loader.getController();
            ObservableList<String> loaiPhiList = FXCollections.observableArrayList();
            loaiPhiList.add("Phí thu");loaiPhiList.add("Đóng góp");
            addFeeController.optionFeeCBox.setItems(loaiPhiList);
            addFeeController.namKhoanPhiAddFee.setEditable(false);
            addFeeController.tenKhoanPhiMoiAddFee.setEditable(false);

            //Cài đặt để có thể di chuyển stage bằng kéo thả
            addFeeRoot.setOnMousePressed((MouseEvent e) -> {
                x = e.getScreenX() - addFeeStage.getX();
                y = e.getScreenY() - addFeeStage.getY();
            });

            addFeeRoot.setOnMouseDragged((MouseEvent e) -> {
                addFeeStage.setX(e.getScreenX() - x);
                addFeeStage.setY(e.getScreenY() - y);
            });

            // Đặt kiểu modality của Stage mới là NONE
            addFeeStage.initModality(Modality.NONE);
            addFeeStage.initStyle(StageStyle.TRANSPARENT);

            // Hiển thị Stage mới
            addFeeStage.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @FXML
    private TextField namKhoanPhiAddFee;
    @FXML
    private DatePicker ngayBatDauThuKhoanPhiMoiAddFee;
    @FXML
    private ComboBox<String> optionFeeCBox;
    @FXML
    private TextField tenKhoanPhiMoiAddFee;
    // Thay đổi giá trị các trường khi chọn các option của loại phí trong giao diện Tạo khoản phí mới
    @FXML
    void getLoaiPhiAddFee(ActionEvent event) {
        if(optionFeeCBox.getValue() != null) {
            if(optionFeeCBox.getValue().equals("Phí thu")) {
                tenKhoanPhiMoiAddFee.setText("Phí vệ sinh ");
                tenKhoanPhiMoiAddFee.setEditable(false);
                namKhoanPhiAddFee.setText("");
                namKhoanPhiAddFee.setEditable(true);
            }
            else {
                tenKhoanPhiMoiAddFee.setText("");
                tenKhoanPhiMoiAddFee.setEditable(true);
                namKhoanPhiAddFee.setText("");
                namKhoanPhiAddFee.setEditable(false);
            }
        }
        else {
            tenKhoanPhiMoiAddFee.setText("");
            tenKhoanPhiMoiAddFee.setEditable(true);
            namKhoanPhiAddFee.setText("");
            namKhoanPhiAddFee.setEditable(false);
        }

    }
    // Cancel tạo khoản phí mới
    @FXML
    void cancelAddFee(MouseEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        newStages.remove(stage);
        stage.close();
    }
    // Nhấn hoàn thành tạo khoản phí mới
    @FXML
    void finishAddFee(MouseEvent event) {
        if(optionFeeCBox.getValue() == null || tenKhoanPhiMoiAddFee.getText().isEmpty() || ngayBatDauThuKhoanPhiMoiAddFee.getValue() == null) {
            Alert alert;
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Nhập thiếu dữ liệu");
            alert.showAndWait();
        }
        else {
            if(optionFeeCBox.getValue().equals("Phí thu") && namKhoanPhiAddFee.getText().isEmpty()) {
                Alert alert;
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Nhập thiếu dữ liệu");
                alert.showAndWait();
            }
            else {
                DatabaseConnector databaseConnector = new DatabaseConnector();

                String LoaiPhi = optionFeeCBox.getValue();
                String TenKhoanPhi = tenKhoanPhiMoiAddFee.getText();
                String NamKhoanPhi = namKhoanPhiAddFee.getText();
                if(LoaiPhi.equals("Phí thu")) TenKhoanPhi += NamKhoanPhi;
                if(databaseConnector.checkExistTenKhoanPhi(TenKhoanPhi) ) {
                    Alert alert;
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Đã tồn tại khoản phí này");
                    alert.showAndWait();
                }
                else if(!checkFormatYear(NamKhoanPhi) && LoaiPhi.equals("Phí thu")) {
                    Alert alert;
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Nhập sai định dạng của năm");
                    alert.showAndWait();
                }
                else {
                    String NgayBatDauThu = ngayBatDauThuKhoanPhiMoiAddFee.getValue().toString();
                    databaseConnector.addNewKhoanPhi(TenKhoanPhi,LoaiPhi,NgayBatDauThu);
                    if(LoaiPhi.equals("Phí thu")) {
                        int IDPhi = databaseConnector.getIDPhi(TenKhoanPhi);
                        databaseConnector.callProcTongHopPhiVeSinh(IDPhi,NamKhoanPhi);
                    }
                    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    Alert alert;
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successful");
                    alert.setHeaderText(null);
                    alert.setContentText("Tạo thành công khoản phí mới");
                    alert.showAndWait();
                    stage.close();
                }

            }
        }
    }
    // Hàm kiểm tra format của năm
    public boolean checkFormatYear (String s) {
        if(s != null && s.length() == 4) {
            for(int i=0;i<4;i++) {
                if(s.charAt(i) <= '9' && s.charAt(i) >= '0') {}
                else return false;
            }
            return true;
        }
        else return false;
    }
    @FXML
    void clickTongHopFee(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("detailFee.fxml"));
            Parent detailFeeRoot = loader.load();

            // Tạo một Stage mới
            Stage detailFeeStage = new Stage();
            detailFeeStage.setScene(new Scene(detailFeeRoot));
            newStages.add(detailFeeStage);

            //Cài đặt để có thể di chuyển stage bằng kéo thả
            detailFeeRoot.setOnMousePressed((MouseEvent e) -> {
                x = e.getScreenX() - detailFeeStage.getX();
                y = e.getScreenY() - detailFeeStage.getY();
            });

            detailFeeRoot.setOnMouseDragged((MouseEvent e) -> {
                detailFeeStage.setX(e.getScreenX() - x);
                detailFeeStage.setY(e.getScreenY() - y);
            });

            // Đặt kiểu modality của Stage mới là NONE
            detailFeeStage.initModality(Modality.NONE);
            detailFeeStage.initStyle(StageStyle.TRANSPARENT);

            // Hiển thị Stage mới
            detailFeeStage.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @FXML
    private TableColumn<DetailDongPhi, String> addressColumnDetailFee;
    @FXML
    private TableColumn<DetailDongPhi, Integer> conThieuColumnDetailFee;
    @FXML
    private TableColumn<DetailDongPhi, Integer> daDongColumnDetailFee;
    @FXML
    private TableView<DetailDongPhi> detailFeeTable;
    @FXML
    private TableColumn<DetailDongPhi, String> idHoColumnDetailFee;
    @FXML
    private TextField idHoDetailFee;
    @FXML
    private ComboBox<String> loaiPhiDetailFee;
    @FXML
    private TableColumn<DetailDongPhi, Integer> phaiDongColumnDetailFee;
    @FXML
    private TableColumn<DetailDongPhi, String> tenKhoanPhiColumnDetailFee;
    @FXML
    private ComboBox<String> tenKhoanPhiDetailFee;
    @FXML
    void actionOnClickLoaiPhiDetailFee(MouseEvent event) {
        loaiPhiDetailFee.setItems(FXCollections.observableArrayList("Tất cả","Phí thu","Đóng góp"));
        tenKhoanPhiDetailFee.getItems().clear();
        tenKhoanPhiDetailFee.setValue("");
    }
    @FXML
    void actionOnClickKhoanPhiDetailFee(MouseEvent event) {
        ObservableList<String> tenKhoanPhiList = FXCollections.observableArrayList();
        DatabaseConnector databaseConnector = new DatabaseConnector();

        String type = loaiPhiDetailFee.getValue();
        // Nếu chọn tất cả các loại phí thì thêm option chọn tất cả các khoản phí
        if(type == null || type.equals("Tất cả")) {
            type = "";
            tenKhoanPhiList.add("Tất cả");
        }
        ResultSet resultSet = databaseConnector.getTenKhoanPhiList(type);
        try {
            while(resultSet.next()) {
                tenKhoanPhiList.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tenKhoanPhiDetailFee.setItems(tenKhoanPhiList);
    }
    @FXML
    void clickSearchDetailFee(MouseEvent event) {
        if(detailFeeTable != null) detailFeeTable.getItems().clear();
        ObservableList<DetailDongPhi> dataList = FXCollections.observableArrayList();

        String queriedHoKhauID = idHoDetailFee.getText();
        String queriedLoaiPhi;
        if(loaiPhiDetailFee.getValue() == null) queriedLoaiPhi = "";
        else if(loaiPhiDetailFee.getValue().equals("Tất cả")) queriedLoaiPhi = "";
        else queriedLoaiPhi = loaiPhiDetailFee.getValue();

        String queriedTenKhoanPhi;
        if(tenKhoanPhiDetailFee.getValue() == null) queriedTenKhoanPhi = "";
        else if(tenKhoanPhiDetailFee.getValue().equals("Tất cả")) queriedTenKhoanPhi = "";
        else queriedTenKhoanPhi = tenKhoanPhiDetailFee.getValue();

        DatabaseConnector databaseConnector = new DatabaseConnector();

        ResultSet dongphiData = databaseConnector.getDataFromDongPhiTable(queriedHoKhauID,queriedLoaiPhi,queriedTenKhoanPhi);

        try {
            while(dongphiData.next()) {
                String HoKhauID = dongphiData.getString(1);
                String DiaChi = dongphiData.getString(2);
                String TenKhoanPhi = dongphiData.getString(3);
                Integer DaDong = dongphiData.getInt(4);
                Integer PhaiDong = dongphiData.getInt(5);

                Integer ConThieu;
                if(PhaiDong == 0) ConThieu = 0;
                else ConThieu = PhaiDong - DaDong;
                DetailDongPhi detailDongPhi = new DetailDongPhi(HoKhauID,DiaChi,TenKhoanPhi,DaDong,PhaiDong,ConThieu);
                dataList.add(detailDongPhi);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        idHoColumnDetailFee.setCellValueFactory(new PropertyValueFactory<DetailDongPhi,String>("HoKhauID"));
        addressColumnDetailFee.setCellValueFactory(new PropertyValueFactory<DetailDongPhi,String>("DiaChi"));
        tenKhoanPhiColumnDetailFee.setCellValueFactory(new PropertyValueFactory<DetailDongPhi,String>("KhoanPhi"));
        daDongColumnDetailFee.setCellValueFactory(new PropertyValueFactory<DetailDongPhi,Integer>("DaDong"));
        phaiDongColumnDetailFee.setCellValueFactory(new PropertyValueFactory<DetailDongPhi,Integer>("PhaiDong"));
        conThieuColumnDetailFee.setCellValueFactory(new PropertyValueFactory<DetailDongPhi,Integer>("ConThieu"));

        detailFeeTable.setItems(dataList);

    }
    /*
        Hết Quản lý thu chi
     */
}
