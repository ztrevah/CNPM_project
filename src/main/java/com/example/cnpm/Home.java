package com.example.cnpm;

public class Home {
    String SoHK;
    String TenChuHo;
    String DiaChi;
    String LoaiSo;
    Home(String id,String owner,String address,String type) {
        this.SoHK = id;
        this.TenChuHo = owner;
        this.DiaChi = address;
        this.LoaiSo = type;
    }

    public String getSoHK() {
        return SoHK;
    }
    public String getDiaChi() {
        return DiaChi;
    }
    public String getTenChuHo() {
        return TenChuHo;
    }
    public String getLoaiSo() { return LoaiSo; }
}
