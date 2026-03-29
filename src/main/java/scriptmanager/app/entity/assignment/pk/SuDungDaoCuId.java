package scriptmanager.app.entity.assignment.pk;

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

    private int maHM;
    private int maDaoCu;

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
        return maHM == that.maHM && maDaoCu == that.maDaoCu;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maHM, maDaoCu);
    }
}