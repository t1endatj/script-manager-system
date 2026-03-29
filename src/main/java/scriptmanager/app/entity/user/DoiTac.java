package scriptmanager.app.entity.user;

import jakarta.persistence.*;
import scriptmanager.app.entity.asset.ThietBi;

import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "DoiTac")
public class DoiTac {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maDT;

    private String tenDonVi;
    private String linhVuc;
    private String sdt;

    // Quan hệ 1 - N với ThietBi
    @OneToMany(mappedBy = "doiTac")
    private Set<ThietBi> thietBis = new HashSet<>();

    //Constructor
    public DoiTac(int maDT, String tenDonVi, String linhVuc, String sdt, Set<ThietBi> thietBis) {
        this.maDT = maDT;
        this.tenDonVi = tenDonVi;
        this.linhVuc = linhVuc;
        this.sdt = sdt;
        this.thietBis = thietBis;
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
        this.thietBis = thietBis;
    }
}