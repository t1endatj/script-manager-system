package scriptmanager.entity.asset;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import scriptmanager.entity.core.SuKienTiec;
import scriptmanager.entity.user.DoiTac;
import scriptmanager.entity.assignment.PhanCongThietBi;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ThietBi")
public class ThietBi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaTB")
    private int maTB;

    @NotBlank
    @Size(max = 100)
    @Column(name = "TenTB", nullable = false, length = 100)
    private String tenTB;

    @Min(0)
    @Column(name = "SoLuong")
    private int soLuong;

    @Size(max = 50)
    @Column(name = "TinhTrang", length = 50)
    private String tinhTrang;

    //Quan hệ 1-n với PhanCongThietBi
    @OneToMany(mappedBy = "thietBi", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PhanCongThietBi> phanCongThietBis = new HashSet<>();

    //Quan hệ 1-n với DoiTac
    @NotNull
    @ManyToOne
    @JoinColumn(name = "MaDT", nullable = false)
    private DoiTac doiTac;

    //Constructor
    public ThietBi() {
    }

    public ThietBi(String tenTB, int soLuong, String tinhTrang, DoiTac doiTac) {
        this.tenTB = tenTB;
        this.soLuong = soLuong;
        this.tinhTrang = tinhTrang;
        this.setDoiTac(doiTac);
    }

    //helper method PhanCongThietBi
    public void addPhanCongThietBi(PhanCongThietBi pc) {
        phanCongThietBis.add(pc);
        pc.setThietBi(this);
    }

    public void removePhanCongThietBi(PhanCongThietBi pc) {
        phanCongThietBis.remove(pc);
        pc.setThietBi(null);
    }

    //Getter và setter
    public int getMaTB() {
        return maTB;
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
        this.phanCongThietBis.clear();
        if (phanCongThietBis != null) {
            phanCongThietBis.forEach(this::addPhanCongThietBi);
        }
    }

    public DoiTac getDoiTac() {
        return doiTac;
    }

    public void setDoiTac(DoiTac doiTac) {
        if (this.doiTac != null) {
            this.doiTac.getThietBis().remove(this);
        }
        this.doiTac = doiTac;
        if (doiTac != null) {
            doiTac.getThietBis().add(this);
        }
    }
}
