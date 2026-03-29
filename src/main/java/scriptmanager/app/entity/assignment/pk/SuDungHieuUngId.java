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

    private static final long serialVersionUID = 1L;

    @Column(name = "MaHM")
    private Integer maHM;

    @Column(name = "MaHU")
    private Integer maHU;

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
        return Objects.equals(maHM, that.maHM) &&
                Objects.equals(maHU, that.maHU);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maHM, maHU);
    }

    //getter và setter
    public int getMaHM() {
        return maHM;
    }

    public void setMaHM(int maHM) {
        this.maHM = maHM;
    }

    public int getMaHU() {
        return maHU;
    }

    public void setMaHU(int maHU) {
        this.maHU = maHU;
    }
}
