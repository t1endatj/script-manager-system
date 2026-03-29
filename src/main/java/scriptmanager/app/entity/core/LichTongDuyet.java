package scriptmanager.app.entity.core;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "LichTongDuyet")
public class LichTongDuyet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaTongDuyet")
    private int maTongDuyet;

    @Column(name = "ThoiGianDuyet")
    private LocalDateTime thoiGianDuyet;

    @Size(max = 65535)
    @Column(name = "NoiDungDuyet", columnDefinition = "TEXT")
    private String noiDungDuyet;

    @Size(max = 50)
    @Column(name = "TrangThai", length = 50)
    private String trangThai;

    //Quan hệ 1-n với SuKienTiec
    @NotNull
    @ManyToOne
    @JoinColumn(name = "MaSK", nullable = false)
    private SuKienTiec suKienTiec;

    //Constructor
    public LichTongDuyet() {
    }

    public LichTongDuyet(LocalDateTime thoiGianDuyet, String noiDungDuyet, String trangThai, SuKienTiec suKienTiec) {
        this.thoiGianDuyet = thoiGianDuyet;
        this.noiDungDuyet = noiDungDuyet;
        this.trangThai = trangThai;
        this.setSuKienTiec(suKienTiec);
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
        if (this.suKienTiec != null) {
            this.suKienTiec.getLichTongDuyets().remove(this);
        }
        this.suKienTiec = suKienTiec;
        if (suKienTiec != null) {
            suKienTiec.getLichTongDuyets().add(this);
        }
    }
}
