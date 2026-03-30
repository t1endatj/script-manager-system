package scriptmanager.entity.core;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import scriptmanager.entity.asset.DanhSachNhac;
import scriptmanager.entity.assignment.PhanCongNhanSu;
import scriptmanager.entity.assignment.PhanCongThietBi;
import scriptmanager.entity.assignment.SuDungDaoCu;
import scriptmanager.entity.assignment.SuDungHieuUng;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "HangMucKichBan")
public class HangMucKichBan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaHM")
    private int maHM;

    @NotBlank
    @Size(max = 100)
    @Column(name = "TenHM", nullable = false, length = 100)
    private String tenHM;

    @Column(name = "TgBatDau")
    private LocalDateTime tgBatDau;

    @Column(name = "TgKetThuc")
    private LocalDateTime tgKetThuc;

    @Size(max = 65535)
    @Column(name = "NoiDung", columnDefinition = "TEXT")
    private String noiDung;

    //Quan hệ 1-n với SuKienTiec
    @NotNull
    @ManyToOne
    @JoinColumn(name = "MaSK", nullable = false)
    private SuKienTiec suKienTiec;

    //Quan hệ 1-n với SuDungDaoCu
    @OneToMany(mappedBy = "hangMuc", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SuDungDaoCu> suDungDaoCus = new HashSet<>();

    //Quan hệ 1-n với SuDungHieuUng
    @OneToMany(mappedBy = "hangMuc", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SuDungHieuUng> suDungHieuUngs = new HashSet<>();

    //Quan hệ 1-n với PhanCongNhanSu
    @OneToMany(mappedBy = "hangMuc", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PhanCongNhanSu> phanCongNhanSus = new HashSet<>();

    //Quan hệ 1-n với PhanCongThietBi
    @OneToMany(mappedBy = "hangMuc", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PhanCongThietBi> phanCongThietBis = new HashSet<>();

    //Quan hệ 1-n với DanhSachNhac
    @OneToMany(mappedBy = "hangMuc", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DanhSachNhac> danhSachNhacs = new HashSet<>();

    //Constructor
    public HangMucKichBan() {
    }

    public HangMucKichBan(String tenHM,SuKienTiec suKienTiec) {
        this.tenHM = tenHM;
        this.setSuKienTiec(suKienTiec);
    }

    //Helper method

    //PhanCongNhanSu
    public void addPhanCongNhanSu(PhanCongNhanSu pc) {
        phanCongNhanSus.add(pc);
        pc.setHangMuc(this);
    }

    public void removePhanCongNhanSu(PhanCongNhanSu pc) {
            phanCongNhanSus.remove(pc);
            pc.setHangMuc(null);
    }

    //PhanCongThietBi
    public void addPhanCongThietBi(PhanCongThietBi pc) {
        phanCongThietBis.add(pc);
        pc.setHangMuc(this);
    }

    public void removePhanCongThietBi(PhanCongThietBi pc) {
            phanCongThietBis.remove(pc);
            pc.setHangMuc(null);
    }

    //SuDungDaoCu
    public void addSuDungDaoCu(SuDungDaoCu sd) {
        suDungDaoCus.add(sd);
        sd.setHangMuc(this);
    }

    public void removeSuDungDaoCu(SuDungDaoCu sd) {
            suDungDaoCus.remove(sd);
            sd.setHangMuc(null);
    }

    //SuDungHieuUng
    public void addSuDungHieuUng(SuDungHieuUng sd) {
        suDungHieuUngs.add(sd);
        sd.setHangMuc(this);
    }

    public void removeSuDungHieuUng(SuDungHieuUng sd) {
            suDungHieuUngs.remove(sd);
            sd.setHangMuc(null);
    }

    //DanhSachNhac
    public void addDanhSachNhac(DanhSachNhac nhac) {
        danhSachNhacs.add(nhac);
        nhac.setHangMuc(this);
    }

    public void removeDanhSachNhac(DanhSachNhac nhac) {
            danhSachNhacs.remove(nhac);
            nhac.setHangMuc(null);
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
        if (this.suKienTiec != null) {
            this.suKienTiec.getHangMucs().remove(this);
        }
        this.suKienTiec = suKienTiec;
        if (suKienTiec != null) {
            suKienTiec.getHangMucs().add(this);
        }
    }

    public Set<SuDungDaoCu> getSuDungDaoCus() {
        return suDungDaoCus;
    }

    public void setSuDungDaoCus(Set<SuDungDaoCu> suDungDaoCus) {
        this.suDungDaoCus.clear();
        if (suDungDaoCus != null) {
            suDungDaoCus.forEach(this::addSuDungDaoCu);
        }
    }

    public Set<SuDungHieuUng> getSuDungHieuUngs() {
        return suDungHieuUngs;
    }

    public void setSuDungHieuUngs(Set<SuDungHieuUng> suDungHieuUngs) {
        this.suDungHieuUngs.clear();
        if (suDungHieuUngs != null) {
            suDungHieuUngs.forEach(this::addSuDungHieuUng);
        }
    }

    public Set<PhanCongNhanSu> getPhanCongNhanSus() {
        return phanCongNhanSus;
    }

    public void setPhanCongNhanSus(Set<PhanCongNhanSu> phanCongNhanSus) {
        this.phanCongNhanSus.clear();
        if (phanCongNhanSus != null) {
            phanCongNhanSus.forEach(this::addPhanCongNhanSu);
        }
    }

    public Set<PhanCongThietBi> getPhanCongThietBis() {
        return phanCongThietBis;
    }

    public void setPhanCongThietBis(Set<PhanCongThietBi> phanCongThietBis) {
        this.phanCongThietBis.clear();
        if (phanCongThietBis != null) {
            phanCongThietBis.forEach(this::addPhanCongThietBi);
        }
    }

    public Set<DanhSachNhac> getDanhSachNhacs() {
        return danhSachNhacs;
    }

    public void setDanhSachNhacs(Set<DanhSachNhac> danhSachNhacs) {
        this.danhSachNhacs.clear();
        if (danhSachNhacs != null) {
            danhSachNhacs.forEach(this::addDanhSachNhac);
        }
    }
}