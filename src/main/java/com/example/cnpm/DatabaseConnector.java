package com.example.cnpm;

import java.sql.*;

public class DatabaseConnector {
    Connection connection;

    // Kết nối với server của db
    public void connect() {
        String url = "jdbc:mysql://localhost:3306/cnpm";
        String username = "chien";
        String password = "1234";
        try {
            connection = DriverManager.getConnection(url,username,password);
            System.out.println("Connect to database successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Thêm nhân khẩu mới vào danh sách nhân khẩu
    public void addPerson (String id,String HoTen,String BiDanh,String NgaySinh,String NoiSinh,String GioiTinh,String NgheNghiep,String QueQuan,String DanToc,String QuocTich,String NoiLamViec) {
        String sql = "insert into NhanKhau values (?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,id);
            preparedStatement.setString(2,HoTen);
            preparedStatement.setString(3,BiDanh);
            preparedStatement.setString(4,NgaySinh);
            preparedStatement.setString(5,NoiSinh);
            preparedStatement.setString(6,QueQuan);
            preparedStatement.setString(7,GioiTinh);
            preparedStatement.setString(8,DanToc);
            preparedStatement.setString(9,QuocTich);
            preparedStatement.setString(10,NgheNghiep);
            preparedStatement.setString(11,NoiLamViec);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Cập nhật thông tin nhân khẩu
    public void updatePerson (String id,String HoTen,String BiDanh,String NgaySinh,String NoiSinh,String GioiTinh,String NgheNghiep,String QueQuan,String DanToc,String QuocTich,String NoiLamViec) {
        String sql = "update NhanKhau " +
                "set HoTen = ?," +
                " BiDanh = ?," +
                " NgaySinh = ?," +
                " NoiSinh = ?, " +
                " QueQuan = ?," +
                " GioiTinh = ?," +
                " DanToc = ?," +
                " QuocTich = ?," +
                " NgheNghiep = ?," +
                " NoiLamViec = ?" +
                " where CCCD = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,HoTen);
            preparedStatement.setString(2,BiDanh);
            preparedStatement.setString(3,NgaySinh);
            preparedStatement.setString(4,NoiSinh);
            preparedStatement.setString(5,QueQuan);
            preparedStatement.setString(6,GioiTinh);
            preparedStatement.setString(7,DanToc);
            preparedStatement.setString(8,QuocTich);
            preparedStatement.setString(9,NgheNghiep);
            preparedStatement.setString(10,NoiLamViec);
            preparedStatement.setString(11,id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Kiểm tra số CCCD nhập cho nhân khẩu mới đã tồn tại chưa
    public boolean checkExistNhanKhau (String id) {
        String sql = "select * from nhankhau where CCCD = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Lấy thông tin của nhân khẩu theo id
    public ResultSet getPeopleInfo (String id) {
        String sql = "select * from NhanKhau\n" +
                "where CCCD = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,id);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Lấy thông tin lưu trú của 1 người
    public ResultSet getPersonAddress (String id,String state,String fromDate,String toDate) {
        String sql = "select DiaChi,LoaiLuuTru,NgayBatDau,NgayKetThuc\n" +
                "from nhankhau_hokhau,HoKhau\n" +
                "where nhankhau_hokhau.NhanKhauID like ? and nhankhau_hokhau.HoKhauID = HoKhau.SoHK " +
                "and nhankhau_hokhau.LoaiLuuTru like ? and " +
                "((NgayBatDau <= ? and NgayKetThuc >= ?) or (NgayBatDau >= ? and NgayKetThuc <= ?)" +
                " or (NgayBatDau <= ? and NgayKetThuc >= ?) or (NgayBatDau >= ? and NgayKetThuc <= ?))";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,"%"+id+"%");
            preparedStatement.setString(2,"%"+state+"%");
            preparedStatement.setString(3,fromDate);
            preparedStatement.setString(4,toDate);
            preparedStatement.setString(5,fromDate);
            preparedStatement.setString(6,toDate);
            preparedStatement.setString(7,toDate);
            preparedStatement.setString(8,toDate);
            preparedStatement.setString(9,fromDate);
            preparedStatement.setString(10,fromDate);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Lấy danh sách nhân khẩu theo filter gồm id, họ tên, giới tính, tuổi
    public ResultSet getPeopleInfoList (String id, String name,String GioiTinh,int ageFrom, int ageTo) {
        String sql = "select CCCD,HoTen,NgaySinh,GioiTinh\n" +
                "from NhanKhau\n" +
                "where (CCCD like ? or HoTen like ?) and age(NgaySinh) between ? and ? and GioiTinh like ? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,"%"+id+"%");
            preparedStatement.setString(2,"%"+name+"%");
            preparedStatement.setInt(3,ageFrom);
            preparedStatement.setInt(4,ageTo);
            preparedStatement.setString(5,"%"+GioiTinh+"%");

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Lấy danh sách nhân khẩu theo filter
    public ResultSet getHomeList (String id) {
        String sql = "select SoHK,ChuHoID,HoTen,DiaChi\n" +
                "from HoKhau,NhanKhau\n" +
                "where HoKhau.ChuHoID = NhanKhau.CCCD and (ChuHoID like ? or DiaChi like ? or SoHK like ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,"%"+id+"%");
            preparedStatement.setString(2,"%"+id+"%");
            preparedStatement.setString(3,"%"+id+"%");

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Kiểm tra nhân khẩu đã có trong danh sách chưa
    public boolean checkExistNhanKhauInNhanKhauList(String id) {
        String sql = "select * from NhanKhau where CCCD = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Lấy họ tên của nhân khẩu theo id
    public String getHoTen(String id) {
        String sql = "select HoTen from NhanKhau where CCCD = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            String HoTen = null;
            while(resultSet.next()) HoTen = resultSet.getString(1);
            return HoTen;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Insert vào bảng tạm temp_hokhautable
    public void insertNewMemberTempHoKhauTable(String id, String HoTen, String QuanHeChuHo) {
        String sql = "insert into temp_hokhautable values (?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,id);
            preparedStatement.setString(2,HoTen);
            preparedStatement.setString(3,QuanHeChuHo);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Xoá dữ liệu của bảng temp_hokhautable
    public void deleteTempHoKhauTable() {
        String sql = "delete from temp_hokhautable";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Lấy các bản ghi của temp_hokhautable
    public ResultSet getTempHoKhauTable(){
        String sql = "select * from temp_hokhautable";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Xoá 1 người trong temp_hokhautable theo CCCD
    public void deleteMemberFromTempHoKhauTable (String id) {
        String sql = "delete from temp_hokhautable where CCCD = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Thêm hộ khẩu mới
    public void insertNewHoKhau(String idHoKhau,String idChuHo,String DiaChi) {
        String sql = "insert into HoKhau(SoHK,ChuHoID,DiaChi) values (?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,idHoKhau);
            preparedStatement.setString(2,idChuHo);
            preparedStatement.setString(3,DiaChi);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //Kiểm tra số hộ khẩu đã tồn tại chưa
    public boolean checkExistHoKhauInHoKhauList(String id) {
        String sql = "select * from HoKhau where SoHK = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Kiểm tra 1 người có là chủ hô của hộ nào ko
    public boolean checkExistChuHoInHoKhauList(String id) {
        String sql = "select * from HoKhau where ChuHoID = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Kiểm tra người đc định làm chủ hộ đã có trong temp_hokhautable chưa
    public boolean checkExistChuHoInTempHoKhauTable(String id) {
        String sql = "select * from temp_hokhautable where CCCD = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Kiểm tra 1 người có đang thường trú tại hộ nào không
    public boolean checkExistNhanKhauThuongTru(String id) {
        String sql = "select * from nhankhau_hokhau where NhanKhauID = ? and NgayKetThuc = '2100-01-01' and LoaiLuuTru = N'Thường trú'";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Xoá đki thường trú của một người: set NgayKetThuc = curdate()
    public void updateNgayKetThucThuongTru(String id) {
        String sql = "update nhankhau_hokhau set NgayKetThuc = curdate() where NhanKhauID = ? and LoaiLuuTru = N'Thường trú'";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Thêm thông tin về nhân khẩu ở 1 hộ khẩu vào bảng nhankhau_hokhau
    public void insertNewNhanKhauHoKhau(String NhanKhauID,String HoKhauID,String NgayBatDau,String NgayKetThuc,String LoaiLuuTru,String QHChuHo) {
        String sql = "insert into nhankhau_hokhau(NhanKhauID,HoKhauID,NgayBatDau,NgayKetThuc,LoaiLuuTru,QHChuHo) values (?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,NhanKhauID);
            preparedStatement.setString(2,HoKhauID);
            preparedStatement.setString(3,NgayBatDau);
            preparedStatement.setString(4,NgayKetThuc);
            preparedStatement.setString(5,LoaiLuuTru);
            preparedStatement.setString(6,QHChuHo);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Đưa ra thông tin của một hộ khẩu (SoHK,ChuHoID,DiaChi)
    public ResultSet getHomeInfo(String id){
        String sql = "select * from hokhau where SoHK = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Đưa ra danh sách các nhân khẩu hiện tại của một hộ khẩu (NhanKhauID,QHChuHo)
    public ResultSet getCurrentMember(String id) {
        String sql = "select NhanKhauID,QHChuHo from nhankhau_hokhau where HoKhauID = ? and NgayKetThuc = '2100-01-01' and LoaiLuuTru = N'Thường trú'";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Cập nhật thông tin chủ hộ và địa chỉ của 1 hộ
    public void updateOwnerAddressHome (String idHome,String idOwner,String address) {
        String sql = "update hokhau set ChuHoID = ?, DiaChi = ? where SoHK = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,idOwner);
            preparedStatement.setString(2,address);
            preparedStatement.setString(3,idHome);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Kiểm tra nhân khẩu x có trong hộ khẩu y không
    public boolean checkExistNhanKhauHoKhau (String NhanKhauID,String HoKhauID) {
        String sql = "select * from nhankhau_hokhau where NhanKhauID = ? and HoKhauID = ? and LoaiLuuTru = N'Thường trù' and NgayKetThuc = '2100-01-01'";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,NhanKhauID);
            preparedStatement.setString(2,HoKhauID);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Cập nhật quan hệ chủ hộ của 1 nhân khẩu
    public void updateRelation(String NhanKhauID,String HoKhauID,String QHChuHo) {
        String sql = "update nhankhau_hokhau set QHChuHo = ? where NhanKhauID = ? and HoKhauID = ? and LoaiLuuTru = N'Thường trù' and NgayKetThuc = '2100-01-01'";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,QHChuHo);
            preparedStatement.setString(2,NhanKhauID);
            preparedStatement.setString(3,HoKhauID);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Kiểm tra một nhân khẩu trong temp_hokhautable không
    public boolean checkExistNhanKhauTempHoKhauTable (String id) {
        String sql = "select * from temp_hokhautable where CCCD = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Cập nhật quan hệ chủ hộ của người trong bảng tạm
    public void updateRelationTempHoKhauTable(String id,String relation) {
        String sql = "update temp_hokhautable set QuanHeChuHo = ? where CCCD = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,relation);
            preparedStatement.setString(2,id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Kiểm tra một người có là chủ hộ của hộ này hay ko
    public boolean checkChuHoHoKhau(String ChuHoID,String HoKhauID) {
        String sql = "select * from hokhau where ChuHoID = ? and SoHK = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,ChuHoID);
            preparedStatement.setString(2,HoKhauID);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Ngắt kết nối với server của db
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Disconnected from the database.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to disconnect from the database: " + e.getMessage());
        }
    }


}
