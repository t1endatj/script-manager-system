package scriptmanager.app.entity.asset;

import jakarta.persistence.*;
import scriptmanager.app.entity.user.DoiTac;
import scriptmanager.app.entity.assignment.PhanCongThietBi;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ThietBi")
public class ThietBi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maTB;

    private String tenTB;
    private int soLuong;
    private String tinhTrang;

    //Quan hệ 1-n với PhanCongThietBi
    @OneToMany(mappedBy = "thietBi")
    private Set<PhanCongThietBi> phanCongThietBis = new HashSet<>();

    //Quan hệ 1-n với DoiTac
    @ManyToOne
    @JoinColumn(name = "MaDT")
    private DoiTac doiTac;

    //Constructor
    public ThietBi() {
    }

    public ThietBi(int maTB, String tenTB, int soLuong, String tinhTrang, Set<PhanCongThietBi> phanCongThietBis) {
        this.maTB = maTB;
        this.tenTB = tenTB;
        this.soLuong = soLuong;
        this.tinhTrang = tinhTrang;
        this.phanCongThietBis = phanCongThietBis;
    }

    //Getter và setter
    public int getMaTB() {
        return maTB;
    }

    public void setMaTB(int maTB) {
        this.maTB = maTB;
    }

    public String getTenTB() {
        return tenTB;
    }

    public void setTenTB(String tenTB) {
        this.tenTB = tenTB;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(String tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public Set<PhanCongThietBi> getPhanCongThietBis() {
        return phanCongThietBis;
    }

    public void setPhanCongThietBis(Set<PhanCongThietBi> phanCongThietBis) {
        this.phanCongThietBis = phanCongThietBis;
    }
}
