package scriptmanager.app.entity.user;

import jakarta.persistence.*;
import scriptmanager.app.entity.core.SuKienTiec;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "NguoiDung")
public class NguoiDung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maND;

    private String tenDangNhap;
    private String matKhau;
    private String quyenHan;

    //Quan hệ 1-n với SuKienTiec
    @OneToMany(mappedBy = "nguoiDung")
    private Set<SuKienTiec> suKienTiecs = new HashSet<>();

    //Constructor
    public NguoiDung() {
    }

    public NguoiDung(int maND, String tenDangNhap, String matKhau, String quyenHan, Set<SuKienTiec> suKienTiecs) {
        this.maND = maND;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.quyenHan = quyenHan;
        this.suKienTiecs = suKienTiecs;
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
        this.suKienTiecs = suKienTiecs;
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