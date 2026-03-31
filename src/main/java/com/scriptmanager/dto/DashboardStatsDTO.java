package com.scriptmanager.dto;

public class DashboardStatsDTO {
    private long suKienSapToi;
    private long tongDuyetChuaXong;
    private long thietBiNguyCoThieu;
    private long nhanSuDaPhanCong;

    public DashboardStatsDTO(long suKienSapToi, long tongDuyetChuaXong, long thietBiNguyCoThieu, long nhanSuDaPhanCong) {
        this.suKienSapToi = suKienSapToi;
        this.tongDuyetChuaXong = tongDuyetChuaXong;
        this.thietBiNguyCoThieu = thietBiNguyCoThieu;
        this.nhanSuDaPhanCong = nhanSuDaPhanCong;
    }

    public long getSuKienSapToi() {
        return suKienSapToi;
    }

    public void setSuKienSapToi(long suKienSapToi) {
        this.suKienSapToi = suKienSapToi;
    }

    public long getTongDuyetChuaXong() {
        return tongDuyetChuaXong;
    }

    public void setTongDuyetChuaXong(long tongDuyetChuaXong) {
        this.tongDuyetChuaXong = tongDuyetChuaXong;
    }

    public long getThietBiNguyCoThieu() {
        return thietBiNguyCoThieu;
    }

    public void setThietBiNguyCoThieu(long thietBiNguyCoThieu) {
        this.thietBiNguyCoThieu = thietBiNguyCoThieu;
    }

    public long getNhanSuDaPhanCong() {
        return nhanSuDaPhanCong;
    }

    public void setNhanSuDaPhanCong(long nhanSuDaPhanCong) {
        this.nhanSuDaPhanCong = nhanSuDaPhanCong;
    }
}
