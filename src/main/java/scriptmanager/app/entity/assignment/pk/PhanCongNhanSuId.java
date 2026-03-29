package scriptmanager.app.entity.assignment.pk;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

// Vì PhanCongNhanSu là entity trung gian, không có thuộc tính khóa chính
// Mà khóa chính là sự kết hợp của MaHM (HangMucKichBan) và MaNS (NhanSu)
// Đây là class dùng làm khóa chính "nhúng" (composite key) của bảng PhanCongNhanSu

@Embeddable
public class PhanCongNhanSuId implements Serializable {

    private int maHM;

    private int maNS;

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
        return maHM == that.maHM && maNS == that.maNS;
    }

    // hashCode: dùng cho HashSet, Map (bắt buộc phải override)
    @Override
    public int hashCode() {
        return Objects.hash(maHM, maNS);
    }
}
