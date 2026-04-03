package scriptmanager.dto;

public class DashboardResourceAlertDTO {
    private final String tenTaiNguyen;
    private final int daDat;
    private final int tongSo;
    private final int phanTram;

    public DashboardResourceAlertDTO(String tenTaiNguyen, int daDat, int tongSo) {
        this.tenTaiNguyen = tenTaiNguyen;
        this.daDat = daDat;
        this.tongSo = tongSo;
        this.phanTram = tongSo <= 0 ? 0 : Math.min(100, (int) Math.round((daDat * 100.0) / tongSo));
    }

    public String getTenTaiNguyen() {
        return tenTaiNguyen;
    }

    public int getDaDat() {
        return daDat;
    }

    public int getTongSo() {
        return tongSo;
    }

    public int getPhanTram() {
        return phanTram;
    }
}
