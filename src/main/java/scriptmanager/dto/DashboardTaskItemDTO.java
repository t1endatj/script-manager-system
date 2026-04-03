package scriptmanager.dto;

import java.time.LocalDateTime;

public class DashboardTaskItemDTO {
    private final String hangMuc;
    private final String suKien;
    private final String nguoiPhuTrach;
    private final LocalDateTime mocGio;
    private final String trangThai;

    public DashboardTaskItemDTO(String hangMuc, String suKien, String nguoiPhuTrach, LocalDateTime mocGio, String trangThai) {
        this.hangMuc = hangMuc;
        this.suKien = suKien;
        this.nguoiPhuTrach = nguoiPhuTrach;
        this.mocGio = mocGio;
        this.trangThai = trangThai;
    }

    public String getHangMuc() {
        return hangMuc;
    }

    public String getSuKien() {
        return suKien;
    }

    public String getNguoiPhuTrach() {
        return nguoiPhuTrach;
    }

    public LocalDateTime getMocGio() {
        return mocGio;
    }

    public String getTrangThai() {
        return trangThai;
    }
}
