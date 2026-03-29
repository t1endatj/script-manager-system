package scriptmanager.app.entity.core;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "LichTongDuyet")
public class LichTongDuyet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maTongDuyet;

    private LocalDateTime thoiGianDuyet;
    private String noiDungDuyet;
    private String trangThai;

    //Quan hệ 1-n với SuKienTiec
    @ManyToOne
    @JoinColumn(name = "MaSK")
    private SuKienTiec suKienTiec;

    //Constructor
    public LichTongDuyet() {
    }

    public LichTongDuyet(int maTongDuyet, LocalDateTime thoiGianDuyet, String noiDungDuyet, String trangThai, SuKienTiec suKienTiec) {
        this.maTongDuyet = maTongDuyet;
        this.thoiGianDuyet = thoiGianDuyet;
        this.noiDungDuyet = noiDungDuyet;
        this.trangThai = trangThai;
        this.suKienTiec = suKienTiec;
    }

    //Getter và setter
    public int getMaTongDuyet() {
        return maTongDuyet;
    }

    public void setMaTongDuyet(int maTongDuyet) {
        this.maTongDuyet = maTongDuyet;
    }

    public LocalDateTime getThoiGianDuyet() {
        return thoiGianDuyet;
    }

    public void setThoiGianDuyet(LocalDateTime thoiGianDuyet) {
        this.thoiGianDuyet = thoiGianDuyet;
    }

    public String getNoiDungDuyet() {
        return noiDungDuyet;
    }

    public void setNoiDungDuyet(String noiDungDuyet) {
        this.noiDungDuyet = noiDungDuyet;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public SuKienTiec getSuKienTiec() {
        return suKienTiec;
    }

    public void setSuKienTiec(SuKienTiec suKienTiec) {
        this.suKienTiec = suKienTiec;
    }
}
