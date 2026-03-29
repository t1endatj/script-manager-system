package scriptmanager.app.entity.assignment.pk;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/*
 * Vì SuDungHieuUng là entity trung gian, không có thuộc tính khóa chính
 * Class đại diện khóa chính kết hợp cho bảng SuDungHieuUng
 * Khóa chính gồm:
 *  - MaHM (HangMucHieuUng)
 *  - MaHU (HieuUng)
 */

@Embeddable
public class SuDungHieuUngId implements Serializable {

    private int maHM;
    private int maHU;

    //Constructor
    public SuDungHieuUngId() {}

    public SuDungHieuUngId(int maHM, int maHU) {
        this.maHM = maHM;
        this.maHU = maHU;
    }

    // equals: dùng để so sánh 2 object ID có giống nhau không
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SuDungHieuUngId)) return false;
        SuDungHieuUngId that = (SuDungHieuUngId) o;
        return maHM == that.maHM && maHU == that.maHU;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maHM, maHU);
    }
}
