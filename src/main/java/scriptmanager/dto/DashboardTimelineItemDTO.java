package scriptmanager.dto;

import java.time.LocalDateTime;

public class DashboardTimelineItemDTO {
    private final LocalDateTime thoiGianDuyet;
    private final String tenSuKien;
    private final String noiDung;

    public DashboardTimelineItemDTO(LocalDateTime thoiGianDuyet, String tenSuKien, String noiDung) {
        this.thoiGianDuyet = thoiGianDuyet;
        this.tenSuKien = tenSuKien;
        this.noiDung = noiDung;
    }

    public LocalDateTime getThoiGianDuyet() {
        return thoiGianDuyet;
    }

    public String getTenSuKien() {
        return tenSuKien;
    }

    public String getNoiDung() {
        return noiDung;
    }
}
