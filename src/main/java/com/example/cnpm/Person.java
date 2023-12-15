package com.example.cnpm;

public class Person {
    public String SoCCCD;
    public String HoTen;
    public String NgaySinh;
    public String GioiTinh;
    public String NoiThuongTru;
    public String TinhTrangLuuTru;
    public Person(String id,String name,String dob,String sex) {
        this.SoCCCD = id;
        this.HoTen = name;
        this.NgaySinh = dob;
        this.GioiTinh = sex;
    }
    public Person(String id,String name,String dob,String sex,String address,String state) {
        this.SoCCCD = id;
        this.HoTen = name;
        this.NgaySinh = dob;
        this.GioiTinh = sex;
        this.NoiThuongTru = address;
        this.TinhTrangLuuTru = state;
    }
    public String getSoCCCD(){
        return this.SoCCCD;
    }
    public String getHoTen(){
        return this.HoTen;
    }
    public String getNgaySinh(){
        return this.NgaySinh;
    }
    public String getGioiTinh(){
        return this.GioiTinh;
    }
    public String getNoiThuongTru() { return this.NoiThuongTru; }
    public String getTinhTrangLuuTru(){
        return this.TinhTrangLuuTru;
    }


}
