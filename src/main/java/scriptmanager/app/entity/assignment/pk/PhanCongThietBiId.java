package scriptmanager.app.entity.assignment.pk;

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

    private int maHM;
    private int maTB;

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
        return maHM == that.maHM && maTB == that.maTB;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maHM, maTB);
    }
}
