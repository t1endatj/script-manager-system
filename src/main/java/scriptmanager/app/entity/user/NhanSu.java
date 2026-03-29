package scriptmanager.app.entity.user;

import jakarta.persistence.*;
import scriptmanager.app.entity.assignment.PhanCongNhanSu;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "NhanSu")
public class NhanSu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maNS;

    private String tenNS;
    private String sdt;
    private String vaiTro;

    //Quan hệ 1-n với PhanCongNhanSu
    @OneToMany(mappedBy = "nhanSu")
    private Set<PhanCongNhanSu> phanCongNhanSus = new HashSet<>();

    //Constructor
    public NhanSu() {
    }

    public NhanSu(int maNS, String tenNS, String sdt, String vaiTro, Set<PhanCongNhanSu> phanCongNhanSus) {
        this.maNS = maNS;
        this.tenNS = tenNS;
        this.sdt = sdt;
        this.vaiTro = vaiTro;
        this.phanCongNhanSus = phanCongNhanSus;
    }

    //Getter và setter

    public int getMaNS() {
        return maNS;
    }

    public void setMaNS(int maNS) {
        this.maNS = maNS;
    }

    public String getTenNS() {
        return tenNS;
    }

    public void setTenNS(String tenNS) {
        this.tenNS = tenNS;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    public Set<PhanCongNhanSu> getPhanCongNhanSus() {
        return phanCongNhanSus;
    }

    public void setPhanCongNhanSus(Set<PhanCongNhanSu> phanCongNhanSus) {
        this.phanCongNhanSus = phanCongNhanSus;
    }
}