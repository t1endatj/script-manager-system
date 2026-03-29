package scriptmanager.app.entity.assignment;

import jakarta.persistence.*;
import scriptmanager.app.entity.asset.ThietBi;
import scriptmanager.app.entity.assignment.pk.PhanCongThietBiId;
import scriptmanager.app.entity.core.HangMucKichBan;

@Entity
@Table(name = "PhanCongThietBi")
public class PhanCongThietBi {

    @EmbeddedId
    private PhanCongThietBiId id;

    private int soLuongSuDung;

    //Quan he 1-n với HangMucKichBan
    @ManyToOne
    @MapsId("maHM")
    @JoinColumn(name = "MaHM")
    private HangMucKichBan hangMuc;

    //Quan hệ 1-n với ThietBi
    @ManyToOne
    @MapsId("maTB")
    @JoinColumn(name = "MaTB")
    private ThietBi thietBi;

    //Constructor
    public PhanCongThietBi() {
    }

    public PhanCongThietBi(PhanCongThietBiId id, int soLuongSuDung, HangMucKichBan hangMuc, ThietBi thietBi) {
        this.id = id;
        this.soLuongSuDung = soLuongSuDung;
        this.hangMuc = hangMuc;
        this.thietBi = thietBi;
    }

    //Getter và setter
    public PhanCongThietBiId getId() {
        return id;
    }

    public void setId(PhanCongThietBiId id) {
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

    public ThietBi getThietBi() {
        return thietBi;
    }

    public void setThietBi(ThietBi thietBi) {
        this.thietBi = thietBi;
    }
}