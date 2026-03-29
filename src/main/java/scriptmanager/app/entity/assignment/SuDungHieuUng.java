package scriptmanager.app.entity.assignment;

import jakarta.persistence.*;
import scriptmanager.app.entity.asset.HieuUng;
import scriptmanager.app.entity.assignment.pk.SuDungHieuUngId;
import scriptmanager.app.entity.core.HangMucKichBan;

import java.time.LocalDateTime;

@Entity
@Table(name = "SuDungHieuUng")
public class SuDungHieuUng {

    @EmbeddedId
    private SuDungHieuUngId id;

    private LocalDateTime thoiDiemKichHoat;

    //Quan hệ 1-n với HangMucKichBan
    @ManyToOne
    @MapsId("maHM")
    @JoinColumn(name = "MaHM")
    private HangMucKichBan hangMuc;

    //Quan hệ 1-n với HieuUng
    @ManyToOne
    @MapsId("maHU")
    @JoinColumn(name = "MaHU")
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
    }

    public HieuUng getHieuUng() {
        return hieuUng;
    }

    public void setHieuUng(HieuUng hieuUng) {
        this.hieuUng = hieuUng;
    }
}
