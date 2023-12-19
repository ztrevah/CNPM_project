package com.example.cnpm;

public class DongPhiLog {
    String HoKhauID;
    String DiaChi;
    String KhoanPhi;
    String NgayDong;
    int SoTienDong;
    int DaDong;
    int ConThieu;
    DongPhiLog(String hoKhauID,String diaChi,String tenKhoanPhi,String ngayDong,int soTienDong,int daDong,int conThieu) {
        this.HoKhauID = hoKhauID;
        this.DiaChi = diaChi;
        this.KhoanPhi = tenKhoanPhi;
        this.NgayDong = ngayDong;
        this.SoTienDong = soTienDong;
        this.DaDong = daDong;
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
    public String getNgayDong() {
        return NgayDong;
    }
    public int getSoTienDong() {
        return SoTienDong;
    }
    public int getDaDong() {
        return DaDong;
    }
    public int getConThieu() {
        return ConThieu;
    }
}
