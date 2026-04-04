package scriptmanager.entity.assignment;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import scriptmanager.entity.assignment.pk.PhanCongNhanSuId;
import scriptmanager.entity.core.HangMucKichBan;
import scriptmanager.entity.user.NhanSu;

@Entity
@Table(name = "PhanCongNhanSu")
public class PhanCongNhanSu {

    @NotBlank
    @EmbeddedId
    private PhanCongNhanSuId id;

    @Size(max = 255)
    @Column(name = "NhiemVu", length = 255)
    private String nhiemVu;

    //Quan hệ 1-n với HangMucKichBan
    @ManyToOne
    @MapsId("maHM")
    @JoinColumn(name = "MaHM", nullable = false)
    private HangMucKichBan hangMuc;

    //Quan hệ 1-n với NhanSu
    @ManyToOne
    @MapsId("maNS")
    @JoinColumn(name = "MaNS", nullable = false)
    private NhanSu nhanSu;

    //Constructor
    public PhanCongNhanSu() {
    }

    public PhanCongNhanSu(String nhiemVu, HangMucKichBan hangMuc, NhanSu nhanSu) {
        this.nhiemVu = nhiemVu;
        this.hangMuc = hangMuc;
        this.nhanSu = nhanSu;
    }

    //Getter và setter
    public PhanCongNhanSuId getId() {
        return id;
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
        if (hangMuc != null) {
            if (this.id == null) {
                this.id = new PhanCongNhanSuId();
            }
            this.id.setMaHM(hangMuc.getMaHM());
        }
    }

    public NhanSu getNhanSu() {
        return nhanSu;
    }

    public void setNhanSu(NhanSu nhanSu) {
        this.nhanSu = nhanSu;
        if (nhanSu != null) {
            if (this.id == null) {
                this.id = new PhanCongNhanSuId();
            }
            this.id.setMaNS(nhanSu.getMaNS());
        }
    }
}
