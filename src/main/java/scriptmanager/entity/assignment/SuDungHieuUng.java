package scriptmanager.entity.assignment;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import scriptmanager.entity.asset.HieuUng;
import scriptmanager.entity.assignment.pk.SuDungHieuUngId;
import scriptmanager.entity.core.HangMucKichBan;

import java.time.LocalDateTime;

@Entity
@Table(name = "SuDungHieuUng")
public class SuDungHieuUng {

    @NotNull
    @EmbeddedId
    private SuDungHieuUngId id;

    @Column(name = "ThoiDiemKichHoat", nullable = false)
    private LocalDateTime thoiDiemKichHoat;

    //Quan hệ 1-n với HangMucKichBan
    @NotNull
    @ManyToOne
    @MapsId("maHM")
    @JoinColumn(name = "MaHM", nullable = false)
    private HangMucKichBan hangMuc;

    //Quan hệ 1-n với HieuUng
    @NotNull
    @ManyToOne
    @MapsId("maHU")
    @JoinColumn(name = "MaHU", nullable = false)
    private HieuUng hieuUng;

    //Constructor
    public SuDungHieuUng() {
    }

    public SuDungHieuUng(SuDungHieuUngId id, LocalDateTime thoiDiemKichHoat, HangMucKichBan hangMuc, HieuUng hieuUng) {
        this.id = id;
        this.thoiDiemKichHoat = thoiDiemKichHoat;
        this.hangMuc = hangMuc;
        this.hieuUng = hieuUng;
    }

    //Getter và setter
    public SuDungHieuUngId getId() {
        return id;
    }

    public void setId(SuDungHieuUngId id) {
        this.id = id;
    }

    public LocalDateTime getThoiDiemKichHoat() {
        return thoiDiemKichHoat;
    }

    public void setThoiDiemKichHoat(LocalDateTime thoiDiemKichHoat) {
        this.thoiDiemKichHoat = thoiDiemKichHoat;
    }

    public HangMucKichBan getHangMuc() {
        return hangMuc;
    }

    public void setHangMuc(HangMucKichBan hangMuc) {
        this.hangMuc = hangMuc;
        if (this.id == null) {
            this.id = new SuDungHieuUngId();
        }
        this.id.setMaHM(hangMuc != null ? hangMuc.getMaHM() : 0);
    }

    public HieuUng getHieuUng() {
        return hieuUng;
    }

    public void setHieuUng(HieuUng hieuUng) {
        this.hieuUng = hieuUng;
        if (this.id == null) {
            this.id = new SuDungHieuUngId();
        }
        this.id.setMaHU(hieuUng != null ? hieuUng.getMaHU() : 0);
    }
}
