package com.example.cnpm;

public class PeopleInfoOfHome {
    String SoCCCD;
    String HoTen;
    String QuanHeChuHo;
    PeopleInfoOfHome(String id,String name,String relation) {
        this.SoCCCD = id;
        this.HoTen = name;
        this.QuanHeChuHo = relation;
    }

    public String getHoTen() {
        return HoTen;
    }

    public String getQuanHeChuHo() {
        return QuanHeChuHo;
    }

    public String getSoCCCD() {
        return SoCCCD;
    }
}
