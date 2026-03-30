package scriptmanager.entity.assignment.pk;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/*
 * Vì PhanCongThietBi là entity trung gian, không có thuộc tính khóa chính
 * Class đại diện khóa chính kết hợp của bảng PhanCongThietBi
 * Khóa chính gồm:
 *  - MaHM (HangMucKichBan)
 *  - MaTB (ThietBi)
 */

@Embeddable
public class PhanCongThietBiId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "MaHM")
    private Integer maHM;

    @Column(name = "MaTB")
    private Integer maTB;

    //Constructor
    public PhanCongThietBiId() {}

    public PhanCongThietBiId(int maHM, int maTB) {
        this.maHM = maHM;
        this.maTB = maTB;
    }

    // equals: dùng để so sánh 2 object ID có giống nhau không
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhanCongThietBiId)) return false;
        PhanCongThietBiId that = (PhanCongThietBiId) o;
        return Objects.equals(maHM, that.maHM) &&
                Objects.equals(maTB, that.maTB);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maHM, maTB);
    }

    //Getter và setter
    public int getMaHM() {
        return maHM;
    }

    public void setMaHM(int maHM) {
        this.maHM = maHM;
    }

    public int getMaTB() {
        return maTB;
    }

    public void setMaTB(int maTB) {
        this.maTB = maTB;
    }
}
