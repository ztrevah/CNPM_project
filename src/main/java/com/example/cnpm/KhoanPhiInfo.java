package com.example.cnpm;

public class KhoanPhiInfo {
    String ID;
    String TenKhoanPhi;
    Integer TongTienDaThu;
    String NgayBatDauThu;
    String LoaiKhoanPhi;
    KhoanPhiInfo(String id,String name,Integer totalMoney,String startDate,String type){
        this.ID = id;
        this.TenKhoanPhi = name;
        this.TongTienDaThu = totalMoney;
        this.NgayBatDauThu = startDate;
        this.LoaiKhoanPhi = type;
    }

    public String getID() {
        return ID;
    }

    public String getTenKhoanPhi() {
        return TenKhoanPhi;
    }

    public Integer getTongTienDaThu() {
        return TongTienDaThu;
    }

    public String getNgayBatDauThu() {
        return NgayBatDauThu;
    }

    public String getLoaiKhoanPhi() {
        return LoaiKhoanPhi;
    }
}
