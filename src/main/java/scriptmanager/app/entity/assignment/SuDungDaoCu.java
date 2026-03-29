package scriptmanager.app.entity.assignment;

import jakarta.persistence.*;
import scriptmanager.app.entity.asset.DaoCu;
import scriptmanager.app.entity.assignment.pk.SuDungDaoCuId;
import scriptmanager.app.entity.core.HangMucKichBan;

@Entity
@Table(name = "SuDungDaoCu")
public class SuDungDaoCu {

    @EmbeddedId
    private SuDungDaoCuId id;

    private int soLuongSuDung;

    //Quan hệ 1-n với HangMucKichBan
    @ManyToOne
    @MapsId("maHM")
    @JoinColumn(name = "MaHM")
    private HangMucKichBan hangMuc;

    //Quan hệ 1-n với DaoCu
    @ManyToOne
    @MapsId("maDaoCu")
    @JoinColumn(name = "MaDaoCu")
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
    }

    public DaoCu getDaoCu() {
        return daoCu;
    }

    public void setDaoCu(DaoCu daoCu) {
        this.daoCu = daoCu;
    }
}