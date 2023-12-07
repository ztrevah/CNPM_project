package com.example.cnpm;

import java.time.LocalDate;

//test, cần sửa.....................
public class people {
    private String cmnd;
    private String tenChuHo;
    private String noiThuongTru;
    private int conThieu;
    private int daDong;
    private LocalDate ngayDong;

    public people(String cmnd, String tenChuHo, String noiThuongTru, int conThieu, int daDong, LocalDate ngayDong) {
        this.cmnd = cmnd;
        this.tenChuHo = tenChuHo;
        this.noiThuongTru = noiThuongTru;
        this.conThieu = conThieu;
        this.daDong = daDong;
        this.ngayDong = ngayDong;
    }

    // Getters and setters

    public String getCmnd() {
        return cmnd;
    }

    public void setCmnd(String cmnd) {
        this.cmnd = cmnd;
    }

    public String getTenChuHo() {
        return tenChuHo;
    }

    public void setTenChuHo(String tenChuHo) {
        this.tenChuHo = tenChuHo;
    }

    public String getNoiThuongTru() {
        return noiThuongTru;
    }

    public void setNoiThuongTru(String noiThuongTru) {
        this.noiThuongTru = noiThuongTru;
    }

    public int getConThieu() {
        return conThieu;
    }

    public void setConThieu(int conThieu) {
        this.conThieu = conThieu;
    }

    public int getDaDong() {
        return daDong;
    }

    public void setDaDong(int daDong) {
        this.daDong = daDong;
    }

    public LocalDate getNgayDong() {
        return ngayDong;
    }

    public void setNgayDong(LocalDate ngayDong) {
        this.ngayDong = ngayDong;
    }
}