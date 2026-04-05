package scriptmanager.entity.asset;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import scriptmanager.entity.core.HangMucKichBan;

@Entity
@Table(name = "DanhSachNhac")
public class DanhSachNhac {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaBaiHat")
    private int maBaiHat;

    @NotBlank
    @Size(max = 100)
    @Column(name = "TenBaiHat", nullable = false, length = 100)
    private String tenBaiHat;

    @Size(max = 100)
    @Column(name = "CaSi", length = 100)
    private String caSi;

    @Min(0)
    @Column(name = "ThoiLuong")
    private int thoiLuong;

    @Size(max = 255)
    @Column(name = "FileNhac", length = 255)
    private String fileNhac;

    @Size(max = 255)
    @Column(name = "TenFileGoc", length = 255)
    private String tenFileGoc;

    @Size(max = 100)
    @Column(name = "LoaiFile", length = 100)
    private String loaiFile;

    @Lob
    @Column(name = "NoiDungFile", columnDefinition = "LONGBLOB")
    private byte[] noiDungFile;

    //Quan hệ 1-n với HangMucKichBan
    @NotNull
    @ManyToOne
    @JoinColumn(name = "MaHM", nullable = false)
    private HangMucKichBan hangMuc;

    //Constructor
    public DanhSachNhac() {
    }

    public DanhSachNhac(String tenBaiHat, String caSi, int thoiLuong, String fileNhac, HangMucKichBan hangMuc) {
        this.tenBaiHat = tenBaiHat;
        this.caSi = caSi;
        this.thoiLuong = thoiLuong;
        this.fileNhac = fileNhac;
        this.setHangMuc(hangMuc);
    }

    //Getter và setter
    public int getMaBaiHat() {
        return maBaiHat;
    }

    public String getTenBaiHat() {
        return tenBaiHat;
    }

    public void setTenBaiHat(String tenBaiHat) {
        this.tenBaiHat = tenBaiHat;
    }

    public String getCaSi() {
        return caSi;
    }

    public void setCaSi(String caSi) {
        this.caSi = caSi;
    }

    public int getThoiLuong() {
        return thoiLuong;
    }

    public void setThoiLuong(int thoiLuong) {
        this.thoiLuong = thoiLuong;
    }

    public String getFileNhac() {
        return fileNhac;
    }

    public void setFileNhac(String fileNhac) {
        this.fileNhac = fileNhac;
    }

    public String getTenFileGoc() {
        return tenFileGoc;
    }

    public void setTenFileGoc(String tenFileGoc) {
        this.tenFileGoc = tenFileGoc;
    }

    public String getLoaiFile() {
        return loaiFile;
    }

    public void setLoaiFile(String loaiFile) {
        this.loaiFile = loaiFile;
    }

    public byte[] getNoiDungFile() {
        return noiDungFile;
    }

    public void setNoiDungFile(byte[] noiDungFile) {
        this.noiDungFile = noiDungFile;
    }

    public HangMucKichBan getHangMuc() {
        return hangMuc;
    }

    public void setHangMuc(HangMucKichBan hangMuc) {
        this.hangMuc = hangMuc;
    }
}
