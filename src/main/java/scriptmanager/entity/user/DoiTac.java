package scriptmanager.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import scriptmanager.entity.asset.ThietBi;
import scriptmanager.entity.core.SuKienTiec;

import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "DoiTac")
public class DoiTac {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDT")
    private int maDT;

    @NotBlank
    @Size(max = 100)
    @Column(name = "TenDonVi", nullable = false, length = 100)
    private String tenDonVi;

    @Size(max = 100)
    @Column(name = "LinhVuc", length = 100)
    private String linhVuc;

    @Size(max = 15)
    @Column(name = "SDT", length = 15)
    private String sdt;

    // Quan hệ 1 - N với ThietBi
    @OneToMany(mappedBy = "doiTac", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ThietBi> thietBis = new HashSet<>();

    //Constructor
    public DoiTac() {
    }

    public DoiTac(String tenDonVi, String linhVuc, String sdt) {
        this.tenDonVi = tenDonVi;
        this.linhVuc = linhVuc;
        this.sdt = sdt;
    }

    //helper method ThietBi
    public void addThietBi(ThietBi tb) {
        thietBis.add(tb);
        tb.setDoiTac(this);
    }

    public void removeThietBi(ThietBi tb) {
            thietBis.remove(tb);
            tb.setDoiTac(null);
    }

    //Getter và setter
    public int getMaDT() {
        return maDT;
    }

    public void setMaDT(int maDT) {
        this.maDT = maDT;
    }

    public String getTenDonVi() {
        return tenDonVi;
    }

    public void setTenDonVi(String tenDonVi) {
        this.tenDonVi = tenDonVi;
    }

    public String getLinhVuc() {
        return linhVuc;
    }

    public void setLinhVuc(String linhVuc) {
        this.linhVuc = linhVuc;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public Set<ThietBi> getThietBis() {
        return thietBis;
    }

    public void setThietBis(Set<ThietBi> thietBis) {
        this.thietBis.clear();
        if (thietBis != null) {
            thietBis.forEach(this::addThietBi);
        }
    }
}