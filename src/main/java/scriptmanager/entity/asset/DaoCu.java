package scriptmanager.entity.asset;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import scriptmanager.entity.assignment.PhanCongThietBi;
import scriptmanager.entity.assignment.SuDungDaoCu;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "DaoCu")
public class DaoCu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDaoCu")
    private int maDaoCu;

    @NotBlank
    @Size(max = 100)
    @Column(name = "TenDaoCu", nullable = false, length = 100)
    private String tenDaoCu;

    @Min(0)
    @Column(name = "SoLuong", columnDefinition = "INT DEFAULT 0")
    private int soLuong;

    @Size(max = 50)
    @Column(name = "TrangThai", length = 50)
    private String trangThai;

    //Quan hệ 1-n với SuDungDaoCu
    @OneToMany(mappedBy = "daoCu", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SuDungDaoCu> suDungDaoCus = new HashSet<>();

    //Constructor
    public DaoCu() {
    }

    public DaoCu(String tenDaoCu, int soLuong, String trangThai) {
        this.tenDaoCu = tenDaoCu;
        this.soLuong = soLuong;
        this.trangThai = trangThai;
    }

    //helper method SuDungDaoCu
    public void addSuDungDaoCu(SuDungDaoCu sd) {
        suDungDaoCus.add(sd);
        sd.setDaoCu(this);
    }

    public void removeSuDungDaoCu(SuDungDaoCu sd) {
        suDungDaoCus.remove(sd);
        sd.setDaoCu(null);
    }

    //Getter và setter

    public int getMaDaoCu() {
        return maDaoCu;
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
        this.suDungDaoCus.clear();
        if (suDungDaoCus != null) {
            suDungDaoCus.forEach(this::addSuDungDaoCu);
        }
    }
}
