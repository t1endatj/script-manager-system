package scriptmanager.app.entity.asset;

import jakarta.persistence.*;
import scriptmanager.app.entity.assignment.SuDungDaoCu;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "DaoCu")
public class DaoCu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maDaoCu;

    private String tenDaoCu;
    private int soLuong;
    private String trangThai;

    //Quan hệ 1-n với SuDungDaoCu
    @OneToMany(mappedBy = "daoCu")
    private Set<SuDungDaoCu> suDungDaoCus = new HashSet<>();

    //Constructor
    public DaoCu() {
    }

    public DaoCu(int maDaoCu, String tenDaoCu, int soLuong, String trangThai, Set<SuDungDaoCu> suDungDaoCus) {
        this.maDaoCu = maDaoCu;
        this.tenDaoCu = tenDaoCu;
        this.soLuong = soLuong;
        this.trangThai = trangThai;
        this.suDungDaoCus = suDungDaoCus;
    }

    //Getter và setter

    public int getMaDaoCu() {
        return maDaoCu;
    }

    public void setMaDaoCu(int maDaoCu) {
        this.maDaoCu = maDaoCu;
    }

    public String getTenDaoCu() {
        return tenDaoCu;
    }

    public void setTenDaoCu(String tenDaoCu) {
        this.tenDaoCu = tenDaoCu;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public Set<SuDungDaoCu> getSuDungDaoCus() {
        return suDungDaoCus;
    }

    public void setSuDungDaoCus(Set<SuDungDaoCu> suDungDaoCus) {
        this.suDungDaoCus = suDungDaoCus;
    }
}
