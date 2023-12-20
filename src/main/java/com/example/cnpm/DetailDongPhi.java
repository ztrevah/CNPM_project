package com.example.cnpm;

public class DetailDongPhi {
    String HoKhauID;
    String DiaChi;
    String KhoanPhi;
    int DaDong;
    int PhaiDong;
    int ConThieu;
    DetailDongPhi(String hoKhauID,String diaChi,String tenKhoanPhi,int daDong,int phaiDong,int conThieu) {
        this.HoKhauID = hoKhauID;
        this.DiaChi = diaChi;
        this.KhoanPhi = tenKhoanPhi;
        this.DaDong = daDong;
        this.PhaiDong = phaiDong;
        this.ConThieu = conThieu;
    }
    public String getHoKhauID() {
        return HoKhauID;
    }
    public String getDiaChi() {
        return DiaChi;
    }
    public String getKhoanPhi() {
        return KhoanPhi;
    }
    public int getDaDong() {
        return DaDong;
    }

    public int getPhaiDong() {
        return PhaiDong;
    }

    public int getConThieu() {
        return ConThieu;
    }
}
