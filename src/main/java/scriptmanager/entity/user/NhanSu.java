package scriptmanager.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import scriptmanager.entity.assignment.PhanCongNhanSu;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "NhanSu")
public class NhanSu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaNS")
    private int maNS;

    @NotBlank
    @Size(max = 100)
    @Column(name = "TenNS", nullable = false, length = 100)
    private String tenNS;

    @Size(max = 15)
    @Column(name = "SDT", length = 15)
    private String sdt;

    @Size(max = 50)
    @Column(name = "VaiTro", length = 50)
    private String vaiTro;

    //Quan hệ 1-n với PhanCongNhanSu
    @OneToMany(mappedBy = "nhanSu", cascade = CascadeType.ALL)
    private Set<PhanCongNhanSu> phanCongNhanSus = new HashSet<>();

    //Constructor
    public NhanSu() {
    }

    public NhanSu(String tenNS, String sdt, String vaiTro) {
        this.tenNS = tenNS;
        this.sdt = sdt;
        this.vaiTro = vaiTro;
    }

    //helper method PhanCongNhanSu
    public void addPhanCongNhanSu(PhanCongNhanSu pc) {
        phanCongNhanSus.add(pc);
        pc.setNhanSu(this);
    }

    public void removePhanCongNhanSu(PhanCongNhanSu pc) {
            phanCongNhanSus.remove(pc);
            pc.setNhanSu(null);
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
        this.phanCongNhanSus.clear();
        if (phanCongNhanSus != null) {
            phanCongNhanSus.forEach(this::addPhanCongNhanSu);
        }
    }
}