package scriptmanager.entity.assignment.pk;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

// Vì PhanCongNhanSu là entity trung gian, không có thuộc tính khóa chính
// Mà khóa chính là sự kết hợp của MaHM (HangMucKichBan) và MaNS (NhanSu)
// Đây là class dùng làm khóa chính "nhúng" (composite key) của bảng PhanCongNhanSu

@Embeddable
public class PhanCongNhanSuId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "MaHM")
    private Integer maHM;

    @Column(name = "MaNS")
    private Integer maNS;

    // Constructor
    public PhanCongNhanSuId() {}

    public PhanCongNhanSuId(int maHM, int maNS) {
        this.maHM = maHM;
        this.maNS = maNS;
    }

    // equals: dùng để so sánh 2 object ID có giống nhau không
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhanCongNhanSuId)) return false;
        PhanCongNhanSuId that = (PhanCongNhanSuId) o;
        return Objects.equals(maHM, that.maHM) &&
                Objects.equals(maNS, that.maNS);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maHM, maNS);
    }

    //getter và setter
    public int getMaNS() {
        return maNS;
    }

    public void setMaNS(int maNS) {
        this.maNS = maNS;
    }

    public int getMaHM() {
        return maHM;
    }

    public void setMaHM(int maHM) {
        this.maHM = maHM;
    }
}
