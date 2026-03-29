package scriptmanager.app.entity.core;

import jakarta.persistence.*;
import scriptmanager.app.entity.asset.DanhSachNhac;
import scriptmanager.app.entity.assignment.PhanCongNhanSu;
import scriptmanager.app.entity.assignment.PhanCongThietBi;
import scriptmanager.app.entity.assignment.SuDungDaoCu;
import scriptmanager.app.entity.assignment.SuDungHieuUng;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "HangMucKichBan")
public class HangMucKichBan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maHM;

    private String tenHM;
    private LocalDateTime tgBatDau;
    private LocalDateTime tgKetThuc;
    private String noiDung;

    //Quan hệ 1-n với SuKienTiec
    @ManyToOne
    @JoinColumn(name = "MaSK")
    private SuKienTiec suKienTiec;

    //Quan hệ 1-n với SuDungDaoCu
    @OneToMany(mappedBy = "hangMuc")
    private Set<SuDungDaoCu> suDungDaoCus = new HashSet<>();

    //Quan hệ 1-n với SuDungHieuUng
    @OneToMany(mappedBy = "hangMuc")
    private Set<SuDungHieuUng> suDungHieuUngs = new HashSet<>();

    //Quan hệ 1-n với PhanCongNhanSu
    @OneToMany(mappedBy = "hangMuc")
    private Set<PhanCongNhanSu> phanCongNhanSus = new HashSet<>();

    //Quan hệ 1-n với PhanCongThietBi
    @OneToMany(mappedBy = "hangMuc")
    private Set<PhanCongThietBi> phanCongThietBis = new HashSet<>();

    //Quan hệ 1-n với DanhSachNhac
    @OneToMany(mappedBy = "hangMuc")
    private Set<DanhSachNhac> danhSachNhacs = new HashSet<>();

    //Constructor
    public HangMucKichBan() {
    }

    public HangMucKichBan(int maHM, String tenHM, LocalDateTime tgBatDau, LocalDateTime tgKetThuc, String noiDung, SuKienTiec suKienTiec, Set<SuDungDaoCu> suDungDaoCus, Set<SuDungHieuUng> suDungHieuUngs, Set<PhanCongNhanSu> phanCongNhanSus, Set<PhanCongThietBi> phanCongThietBis, Set<DanhSachNhac> danhSachNhacs) {
        this.maHM = maHM;
        this.tenHM = tenHM;
        this.tgBatDau = tgBatDau;
        this.tgKetThuc = tgKetThuc;
        this.noiDung = noiDung;
        this.suKienTiec = suKienTiec;
        this.suDungDaoCus = suDungDaoCus;
        this.suDungHieuUngs = suDungHieuUngs;
        this.phanCongNhanSus = phanCongNhanSus;
        this.phanCongThietBis = phanCongThietBis;
        this.danhSachNhacs = danhSachNhacs;
    }

    //Getter và Setter

    public int getMaHM() {
        return maHM;
    }

    public void setMaHM(int maHM) {
        this.maHM = maHM;
    }

    public String getTenHM() {
        return tenHM;
    }

    public void setTenHM(String tenHM) {
        this.tenHM = tenHM;
    }

    public LocalDateTime getTgBatDau() {
        return tgBatDau;
    }

    public void setTgBatDau(LocalDateTime tgBatDau) {
        this.tgBatDau = tgBatDau;
    }

    public LocalDateTime getTgKetThuc() {
        return tgKetThuc;
    }

    public void setTgKetThuc(LocalDateTime tgKetThuc) {
        this.tgKetThuc = tgKetThuc;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public SuKienTiec getSuKienTiec() {
        return suKienTiec;
    }

    public void setSuKienTiec(SuKienTiec suKienTiec) {
        this.suKienTiec = suKienTiec;
    }

    public Set<SuDungDaoCu> getSuDungDaoCus() {
        return suDungDaoCus;
    }

    public void setSuDungDaoCus(Set<SuDungDaoCu> suDungDaoCus) {
        this.suDungDaoCus = suDungDaoCus;
    }

    public Set<SuDungHieuUng> getSuDungHieuUngs() {
        return suDungHieuUngs;
    }

    public void setSuDungHieuUngs(Set<SuDungHieuUng> suDungHieuUngs) {
        this.suDungHieuUngs = suDungHieuUngs;
    }

    public Set<PhanCongNhanSu> getPhanCongNhanSus() {
        return phanCongNhanSus;
    }

    public void setPhanCongNhanSus(Set<PhanCongNhanSu> phanCongNhanSus) {
        this.phanCongNhanSus = phanCongNhanSus;
    }

    public Set<PhanCongThietBi> getPhanCongThietBis() {
        return phanCongThietBis;
    }

    public void setPhanCongThietBis(Set<PhanCongThietBi> phanCongThietBis) {
        this.phanCongThietBis = phanCongThietBis;
    }

    public Set<DanhSachNhac> getDanhSachNhacs() {
        return danhSachNhacs;
    }

    public void setDanhSachNhacs(Set<DanhSachNhac> danhSachNhacs) {
        this.danhSachNhacs = danhSachNhacs;
    }
}