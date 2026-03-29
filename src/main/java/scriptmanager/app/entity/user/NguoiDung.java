package scriptmanager.app.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import scriptmanager.app.entity.core.SuKienTiec;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "NguoiDung")
public class NguoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaND")
    private int maND;

    @NotBlank
    @Size(max = 50)
    @Column(name = "TenDangNhap", nullable = false, unique = true, length = 50)
    private String tenDangNhap;

    @NotBlank
    @Size(max = 100)
    @Column(name = "MatKhau", nullable = false, length = 100)
    private String matKhau;

    @NotBlank
    @Size(max = 50)
    @Column(name = "QuyenHan", nullable = false, length = 50)
    private String quyenHan;

    //Quan hệ 1-n với SuKienTiec
    @OneToMany(mappedBy = "nguoiDung", cascade = CascadeType.ALL)
    private Set<SuKienTiec> suKienTiecs = new HashSet<>();

    //Constructor
    public NguoiDung() {
    }

    public NguoiDung(String tenDangNhap, String matKhau, String quyenHan) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.quyenHan = quyenHan;
    }

    //Helper method SuKienTiec
    public void addSuKienTiec(SuKienTiec skt) {
        suKienTiecs.add(skt);
        skt.setNguoiDung(this);
    }

    public void removeSuKienTiec(SuKienTiec skt) {
            suKienTiecs.remove(skt);
            skt.setNguoiDung(null);
    }

    //Getter
    public int getMaND() {
        return maND;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public String getQuyenHan() {
        return quyenHan;
    }

    public Set<SuKienTiec> getSuKienTiecs() {
        return suKienTiecs;
    }

    // Setter
    public void setSuKienTiecs(Set<SuKienTiec> suKienTiecs) {
        this.suKienTiecs.clear();
        if (suKienTiecs != null) {
            suKienTiecs.forEach(this::addSuKienTiec);
        }
    }

    public void setQuyenHan(String quyenHan) {
        this.quyenHan = quyenHan;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public void setMaND(int maND) {
        this.maND = maND;
    }
}