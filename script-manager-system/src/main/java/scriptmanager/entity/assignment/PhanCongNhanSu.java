package scriptmanager.entity.assignment;

import jakarta.persistence.*;
import scriptmanager.entity.assignment.pk.PhanCongNhanSuId;
import scriptmanager.entity.core.HangMucKichBan;
import scriptmanager.entity.user.NhanSu;

@Entity
@Table(name = "PhanCongNhanSu")
public class PhanCongNhanSu {

    @EmbeddedId
    private PhanCongNhanSuId id = new PhanCongNhanSuId();

    @Column(name = "NhiemVu", length = 255)
    private String nhiemVu;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("maHM")
    @JoinColumn(name = "MaHM", nullable = false)
    private HangMucKichBan hangMuc;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("maNS")
    @JoinColumn(name = "MaNS", nullable = false)
    private NhanSu nhanSu;

    public PhanCongNhanSu() {
    }

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
        if (this.id == null) {
            this.id = new PhanCongNhanSuId();
        }
        if (hangMuc != null) {
            this.id.setMaHM(hangMuc.getMaHM());
        }
    }

    public NhanSu getNhanSu() {
        return nhanSu;
    }

    public void setNhanSu(NhanSu nhanSu) {
        this.nhanSu = nhanSu;
        if (this.id == null) {
            this.id = new PhanCongNhanSuId();
        }
        if (nhanSu != null) {
            this.id.setMaNS(nhanSu.getMaNS());
        }
    }
}
