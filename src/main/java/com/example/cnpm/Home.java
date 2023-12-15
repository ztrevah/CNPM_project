package com.example.cnpm;

public class Home {
    String SoHK;
    String TenChuHo;
    String DiaChi;
    Home(String id,String owner,String address) {
        this.SoHK = id;
        this.TenChuHo = owner;
        this.DiaChi = address;
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
}
