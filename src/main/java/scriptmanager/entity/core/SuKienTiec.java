package scriptmanager.entity.core;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import scriptmanager.entity.user.NguoiDung;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "SuKienTiec", indexes = {
        @Index(name = "IDX_SK_TEN", columnList = "TenSuKien"),
        @Index(name = "IDX_SK_THOIGIAN", columnList = "ThoiGianToChuc"),
        @Index(name = "IDX_SK_MAND", columnList = "MaND")
})
public class SuKienTiec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaSK")
    private int maSK;

    @NotBlank
    @Size(max = 100)
    @Column(name = "TenSuKien", nullable = false, length = 100)
    private String tenSuKien;

    @Column(name = "ThoiGianToChuc")
    private LocalDateTime thoiGianToChuc;

    @Size(max = 255)
    @Column(name = "DiaDiem", length = 255)
    private String diaDiem;

    //Quan hệ với NguoiDung (N-1)
    @NotNull
    @ManyToOne
    @JoinColumn(name = "MaND", nullable = false)
    private NguoiDung nguoiDung;

    //Quan hệ với HangMucKichBan (1-N)
    @OneToMany(mappedBy = "suKienTiec", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<HangMucKichBan> hangMucs = new HashSet<>();

    //Quan hệ với LichTongDuyet (1-N)
    @OneToMany(mappedBy = "suKienTiec", cascade = CascadeType.ALL, orphanRemoval = true)
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

    //Helper method HangMucKichBan
    public void addHangMuc(HangMucKichBan hm) {
        hangMucs.add(hm);
        hm.setSuKienTiec(this);
    }

    public void removeHangMuc(HangMucKichBan hm) {
            hangMucs.remove(hm);
            hm.setSuKienTiec(null);
    }

    //Helper method LichTongDuyet
    public void addLichTongDuyet(LichTongDuyet ltd) {
        lichTongDuyets.add(ltd);
        ltd.setSuKienTiec(this);
    }

    public void removeLichTongDuyet(LichTongDuyet ltd) {
            lichTongDuyets.remove(ltd);
            ltd.setSuKienTiec(null);
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
        this.hangMucs.clear();
        if (hangMucs != null) {
            hangMucs.forEach(this::addHangMuc);
        }
    }

    public void setLichTongDuyets(Set<LichTongDuyet> lichTongDuyets) {
        this.lichTongDuyets.clear();
        if (lichTongDuyets != null) {
            lichTongDuyets.forEach(this::addLichTongDuyet);
        }
    }


}