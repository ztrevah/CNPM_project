package com.example.cnpm;

public class LichSuThayDoi {
    String NgayThayDoi;
    String NoiDung;
    LichSuThayDoi(String ngayThayDoi,String noiDung) {
        this.NgayThayDoi = ngayThayDoi;
        this.NoiDung = noiDung;
    }

    public String getNgayThayDoi() {
        return NgayThayDoi;
    }

    public String getNoiDung() {
        return NoiDung;
    }
}
