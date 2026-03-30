package scriptmanager.entity.assignment.pk;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/*
 * Vì SuDungDaoCu là entity trung gian, không có thuộc tính khóa chính
 * Class đại diện cho khóa chính kết hợp của bảng SuDungDaoCu
 * Khóa chính gồm:
 *  - MaHM (HangMucKichBan)
 *  - MaDaoCu (DaoCu)
*/

@Embeddable
public class SuDungDaoCuId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "MaHM")
    private Integer maHM;

    @Column(name = "MaDaoCu")
    private Integer maDaoCu;

    //Constructor
    public SuDungDaoCuId() {}

    public SuDungDaoCuId(int maHM, int maDaoCu) {
        this.maHM = maHM;
        this.maDaoCu = maDaoCu;
    }

    // equals: dùng để so sánh 2 object ID có giống nhau không
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SuDungDaoCuId)) return false;
        SuDungDaoCuId that = (SuDungDaoCuId) o;
        return Objects.equals(maHM, that.maHM) &&
                Objects.equals(maDaoCu, that.maDaoCu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maHM, maDaoCu);
    }

    //Getter và setter
    public int getMaHM() {
        return maHM;
    }

    public void setMaHM(int maHM) {
        this.maHM = maHM;
    }

    public int getMaDaoCu() {
        return maDaoCu;
    }

    public void setMaDaoCu(int maDaoCu) {
        this.maDaoCu = maDaoCu;
    }
}