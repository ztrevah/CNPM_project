package com.example.cnpm;

import java.sql.*;

public class DatabaseConnector {
    Connection connection;

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
    public void deleteTempHoKhauTable() {
        String sql = "delete from temp_hokhautable";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
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
