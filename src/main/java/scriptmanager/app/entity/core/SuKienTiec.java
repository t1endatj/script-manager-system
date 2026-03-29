package scriptmanager.app.entity.core;

import jakarta.persistence.*;
import scriptmanager.app.entity.user.NguoiDung;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "SuKienTiec")
public class SuKienTiec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maSK;

    @Column(nullable = false, length = 100)
    private String tenSuKien;

    private LocalDateTime thoiGianToChuc;

    private String diaDiem;

    //Quan hệ với NguoiDung (N-1)
    @ManyToOne
    @JoinColumn(name = "MaND", nullable = false)
    private NguoiDung nguoiDung;

    //Quan hệ với HangMucKichBan (1-N)
    @OneToMany(mappedBy = "suKienTiec", cascade = CascadeType.ALL)
    private Set<HangMucKichBan> hangMucs = new HashSet<>();

    //Quan hệ với LichTongDuyet (1-N)
    @OneToMany(mappedBy = "suKienTiec", cascade = CascadeType.ALL)
    private Set<LichTongDuyet> lichTongDuyets = new HashSet<>();

    //Constructor
    public SuKienTiec() {
    }

    public SuKienTiec(String tenSuKien, LocalDateTime thoiGianToChuc, String diaDiem, NguoiDung nguoiDung) {
        this.tenSuKien = tenSuKien;
        this.thoiGianToChuc = thoiGianToChuc;
        this.diaDiem = diaDiem;
        this.nguoiDung = nguoiDung;
    }

    //Getter & Setter
    public int getMaSK() {
        return maSK;
    }

    public String getTenSuKien() {
        return tenSuKien;
    }

    public void setTenSuKien(String tenSuKien) {
        this.tenSuKien = tenSuKien;
    }

    public LocalDateTime getThoiGianToChuc() {
        return thoiGianToChuc;
    }

    public void setThoiGianToChuc(LocalDateTime thoiGianToChuc) {
        this.thoiGianToChuc = thoiGianToChuc;
    }

    public String getDiaDiem() {
        return diaDiem;
    }

    public void setDiaDiem(String diaDiem) {
        this.diaDiem = diaDiem;
    }

    public NguoiDung getNguoiDung() {
        return nguoiDung;
    }

    public void setNguoiDung(NguoiDung nguoiDung) {
        this.nguoiDung = nguoiDung;
    }

    public Set<HangMucKichBan> getHangMucs() {
        return hangMucs;
    }

    public Set<LichTongDuyet> getLichTongDuyets() {
        return lichTongDuyets;
    }

    public void setMaSK(int maSK) {
        this.maSK = maSK;
    }

    public void setHangMucs(Set<HangMucKichBan> hangMucs) {
        this.hangMucs = hangMucs;
    }

    public void setLichTongDuyets(Set<LichTongDuyet> lichTongDuyets) {
        this.lichTongDuyets = lichTongDuyets;
    }
}