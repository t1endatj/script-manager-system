package scriptmanager.entity.assignment;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import scriptmanager.entity.asset.DaoCu;
import scriptmanager.entity.assignment.pk.SuDungDaoCuId;
import scriptmanager.entity.core.HangMucKichBan;

@Entity
@Table(name = "SuDungDaoCu")
public class SuDungDaoCu {

    @NotNull
    @EmbeddedId
    private SuDungDaoCuId id;

    @Min(1)
    @Column(name = "SoLuongSuDung", nullable = false)
    private int soLuongSuDung;

    //Quan hệ 1-n với HangMucKichBan
    @NotNull
    @ManyToOne
    @MapsId("maHM")
    @JoinColumn(name = "MaHM", nullable = false)
    private HangMucKichBan hangMuc;

    //Quan hệ 1-n với DaoCu
    @NotNull
    @ManyToOne
    @MapsId("maDaoCu")
    @JoinColumn(name = "MaDaoCu", nullable = false)
    private DaoCu daoCu;

    //Constructor
    public SuDungDaoCu() {
    }

    public SuDungDaoCu(SuDungDaoCuId id, int soLuongSuDung, HangMucKichBan hangMuc, DaoCu daoCu) {
        this.id = id;
        this.soLuongSuDung = soLuongSuDung;
        this.hangMuc = hangMuc;
        this.daoCu = daoCu;
    }

    //Getter và setter
    public SuDungDaoCuId getId() {
        return id;
    }

    public void setId(SuDungDaoCuId id) {
        this.id = id;
    }

    public int getSoLuongSuDung() {
        return soLuongSuDung;
    }

    public void setSoLuongSuDung(int soLuongSuDung) {
        this.soLuongSuDung = soLuongSuDung;
    }

    public HangMucKichBan getHangMuc() {
        return hangMuc;
    }

    public void setHangMuc(HangMucKichBan hangMuc) {
        this.hangMuc = hangMuc;
        if (this.id == null) {
            this.id = new SuDungDaoCuId();
        }
        this.id.setMaHM(hangMuc != null ? hangMuc.getMaHM() : 0);
    }

    public DaoCu getDaoCu() {
        return daoCu;
    }

    public void setDaoCu(DaoCu daoCu) {
        this.daoCu = daoCu;
        if (this.id == null) {
            this.id = new SuDungDaoCuId();
        }
        this.id.setMaDaoCu(daoCu != null ? daoCu.getMaDaoCu() : 0);
    }
}