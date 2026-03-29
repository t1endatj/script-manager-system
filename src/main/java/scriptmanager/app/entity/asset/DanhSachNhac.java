package scriptmanager.app.entity.asset;

import jakarta.persistence.*;
import scriptmanager.app.entity.core.HangMucKichBan;

@Entity
@Table(name = "DanhSachNhac")
public class DanhSachNhac {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maBaiHat;

    private String tenBaiHat;
    private String caSi;
    private int thoiLuong;
    private String fileNhac;

    //Quan hệ 1-n với HangMucKichBan
    @ManyToOne
    @JoinColumn(name = "MaHM")
    private HangMucKichBan hangMuc;

    //Constructor
    public DanhSachNhac() {
    }

    public DanhSachNhac(int maBaiHat, String tenBaiHat, String caSi, int thoiLuong, String fileNhac, HangMucKichBan hangMuc) {
        this.maBaiHat = maBaiHat;
        this.tenBaiHat = tenBaiHat;
        this.caSi = caSi;
        this.thoiLuong = thoiLuong;
        this.fileNhac = fileNhac;
        this.hangMuc = hangMuc;
    }

    //Getter và setter
    public int getMaBaiHat() {
        return maBaiHat;
    }

    public void setMaBaiHat(int maBaiHat) {
        this.maBaiHat = maBaiHat;
    }

    public String getTenBaiHat() {
        return tenBaiHat;
    }

    public void setTenBaiHat(String tenBaiHat) {
        this.tenBaiHat = tenBaiHat;
    }

    public String getCaSi() {
        return caSi;
    }

    public void setCaSi(String caSi) {
        this.caSi = caSi;
    }

    public int getThoiLuong() {
        return thoiLuong;
    }

    public void setThoiLuong(int thoiLuong) {
        this.thoiLuong = thoiLuong;
    }

    public String getFileNhac() {
        return fileNhac;
    }

    public void setFileNhac(String fileNhac) {
        this.fileNhac = fileNhac;
    }

    public HangMucKichBan getHangMuc() {
        return hangMuc;
    }

    public void setHangMuc(HangMucKichBan hangMuc) {
        this.hangMuc = hangMuc;
    }
}
