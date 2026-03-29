package scriptmanager.app.entity.assignment;

import jakarta.persistence.*;
import scriptmanager.app.entity.assignment.pk.PhanCongNhanSuId;
import scriptmanager.app.entity.core.HangMucKichBan;
import scriptmanager.app.entity.user.NhanSu;

@Entity
@Table(name = "PhanCongNhanSu")
public class PhanCongNhanSu {

    @EmbeddedId
    private PhanCongNhanSuId id;

    private String nhiemVu;

    //Quan hệ 1-n với HangMucKichBan
    @ManyToOne
    @MapsId("maHM")
    @JoinColumn(name = "MaHM")
    private HangMucKichBan hangMuc;

    //Quan hệ 1-n với NhanSu
    @ManyToOne
    @MapsId("maNS")
    @JoinColumn(name = "MaNS")
    private NhanSu nhanSu;

    //Constructor
    public PhanCongNhanSu() {
    }

    public PhanCongNhanSu(PhanCongNhanSuId id, String nhiemVu, HangMucKichBan hangMuc, NhanSu nhanSu) {
        this.id = id;
        this.nhiemVu = nhiemVu;
        this.hangMuc = hangMuc;
        this.nhanSu = nhanSu;
    }

    //Getter và setter
    public PhanCongNhanSuId getId() {
        return id;
    }

    public void setId(PhanCongNhanSuId id) {
        this.id = id;
    }

    public String getNhiemVu() {
        return nhiemVu;
    }

    public void setNhiemVu(String nhiemVu) {
        this.nhiemVu = nhiemVu;
    }

    public HangMucKichBan getHangMuc() {
        return hangMuc;
    }

    public void setHangMuc(HangMucKichBan hangMuc) {
        this.hangMuc = hangMuc;
    }

    public NhanSu getNhanSu() {
        return nhanSu;
    }

    public void setNhanSu(NhanSu nhanSu) {
        this.nhanSu = nhanSu;
    }
}
