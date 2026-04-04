package scriptmanager.dao;

import scriptmanager.config.HibernateUtil;
import scriptmanager.dto.DashboardResourceAlertDTO;
import scriptmanager.dto.DashboardStatsDTO;
import scriptmanager.dto.DashboardTaskItemDTO;
import scriptmanager.dto.DashboardTimelineItemDTO;
import org.hibernate.Session;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DashboardDao {
    public DashboardStatsDTO getStats() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            long suKienSapToi = (long) session.createQuery("SELECT COUNT(s) FROM SuKienTiec s WHERE s.thoiGianToChuc >= CURRENT_TIMESTAMP").uniqueResult();
            long tongDuyetChuaXong = (long) session.createQuery("SELECT COUNT(l) FROM LichTongDuyet l WHERE l.trangThai != 'Hoàn thành'").uniqueResult();
            long thietBiNguyCoThieu = (long) session.createQuery("SELECT COUNT(t) FROM ThietBi t WHERE t.soLuong < 5").uniqueResult();
            long nhanSuDaPhanCong = (long) session.createQuery("SELECT COUNT(DISTINCT p.nhanSu.maNS) FROM PhanCongNhanSu p").uniqueResult();
            return new DashboardStatsDTO(suKienSapToi, tongDuyetChuaXong, thietBiNguyCoThieu, nhanSuDaPhanCong);
        }
    }

    public List<DashboardTimelineItemDTO> getLatestRehearsals(int limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
            LocalDateTime startOfNextDay = startOfDay.plusDays(1);

            List<Object[]> rows = session.createQuery(
                            "SELECT l.thoiGianDuyet, s.tenSuKien, l.noiDungDuyet " +
                                    "FROM LichTongDuyet l JOIN l.suKienTiec s " +
                                    "WHERE l.thoiGianDuyet >= :startOfDay AND l.thoiGianDuyet < :startOfNextDay " +
                                    "ORDER BY l.thoiGianDuyet ASC",
                            Object[].class)
                    .setParameter("startOfDay", startOfDay)
                    .setParameter("startOfNextDay", startOfNextDay)
                    .setMaxResults(limit)
                    .list();

            List<DashboardTimelineItemDTO> items = new ArrayList<>();
            for (Object[] row : rows) {
                items.add(new DashboardTimelineItemDTO(
                        (LocalDateTime) row[0],
                        (String) row[1],
                        (String) row[2]
                ));
            }
            return items;
        }
    }

    public List<DashboardResourceAlertDTO> getResourceAlerts(int limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<AlertRow> rows = new ArrayList<>();

            List<Object[]> thietBiRows = session.createQuery(
                            "SELECT t.tenTB, COALESCE(SUM(pc.soLuongSuDung), 0), t.soLuong " +
                                    "FROM ThietBi t LEFT JOIN t.phanCongThietBis pc " +
                                    "GROUP BY t.maTB, t.tenTB, t.soLuong",
                            Object[].class)
                    .list();

            for (Object[] row : thietBiRows) {
                int daDat = ((Number) row[1]).intValue();
                int tongSo = ((Number) row[2]).intValue();
                if (tongSo <= 0) {
                    continue;
                }
                rows.add(new AlertRow("Thiết bị: " + row[0], daDat, tongSo));
            }

            List<Object[]> daoCuRows = session.createQuery(
                            "SELECT d.tenDaoCu, COALESCE(SUM(sd.soLuongSuDung), 0), d.soLuong " +
                                    "FROM DaoCu d LEFT JOIN d.suDungDaoCus sd " +
                                    "GROUP BY d.maDaoCu, d.tenDaoCu, d.soLuong",
                            Object[].class)
                    .list();

            for (Object[] row : daoCuRows) {
                int daDat = ((Number) row[1]).intValue();
                int tongSo = ((Number) row[2]).intValue();
                if (tongSo <= 0) {
                    continue;
                }
                rows.add(new AlertRow("Đạo cụ: " + row[0], daDat, tongSo));
            }

            rows.sort(Comparator
                    .comparingInt(AlertRow::getMucDoCanhBao)
                    .reversed()
                    .thenComparingInt(AlertRow::getConLai)
            );

            List<DashboardResourceAlertDTO> result = rows.stream()
                    .filter(AlertRow::isWarning)
                    .map(row -> new DashboardResourceAlertDTO(row.tenTaiNguyen, row.daDat, row.tongSo))
                    .toList();
            return result.size() <= limit ? result : new ArrayList<>(result.subList(0, limit));
        }
    }

    private static class AlertRow {
        private final String tenTaiNguyen;
        private final int daDat;
        private final int tongSo;

        private AlertRow(String tenTaiNguyen, int daDat, int tongSo) {
            this.tenTaiNguyen = tenTaiNguyen;
            this.daDat = daDat;
            this.tongSo = tongSo;
        }

        private int getConLai() {
            return Math.max(0, tongSo - daDat);
        }

        private boolean isWarning() {
            int conLai = tongSo - daDat;
            return daDat == 0 || conLai <= 2 || daDat > tongSo;
        }

        private int getMucDoCanhBao() {
            int conLai = tongSo - daDat;
            if (daDat > tongSo) {
                return 300 + (daDat - tongSo);
            }
            if (conLai <= 0) {
                return 250;
            }
            if (conLai <= 2) {
                return 200 - conLai;
            }
            if (daDat == 0) {
                return 120;
            }
            return 0;
        }
    }

    public List<DashboardTaskItemDTO> getPendingTasks(int limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
            LocalDateTime startOfNextDay = startOfDay.plusDays(1);

            List<Object[]> rows = session.createQuery(
                            "SELECT hm.tenHM, sk.tenSuKien, hm.tgBatDau, COUNT(ns.maNS), MIN(ns.tenNS) " +
                                    "FROM HangMucKichBan hm " +
                                    "JOIN hm.suKienTiec sk " +
                                    "LEFT JOIN hm.phanCongNhanSus pc " +
                                    "LEFT JOIN pc.nhanSu ns " +
                                    "WHERE sk.thoiGianToChuc >= :startOfDay AND sk.thoiGianToChuc < :startOfNextDay " +
                                    "GROUP BY hm.maHM, hm.tenHM, sk.tenSuKien, hm.tgBatDau " +
                                    "ORDER BY hm.tgBatDau ASC",
                            Object[].class)
                    .setParameter("startOfDay", startOfDay)
                    .setParameter("startOfNextDay", startOfNextDay)
                    .setMaxResults(limit)
                    .list();

            List<DashboardTaskItemDTO> items = new ArrayList<>();
            LocalDateTime now = LocalDateTime.now();
            for (Object[] row : rows) {
                String tenHM = (String) row[0];
                String tenSuKien = (String) row[1];
                LocalDateTime tgBatDau = (LocalDateTime) row[2];
                long soNhanSu = ((Number) row[3]).longValue();
                String firstNhanSu = (String) row[4];

                String nguoiPhuTrach;
                if (soNhanSu <= 0) {
                    nguoiPhuTrach = "Chưa phân công";
                } else if (soNhanSu == 1) {
                    nguoiPhuTrach = firstNhanSu;
                } else {
                    nguoiPhuTrach = firstNhanSu + " +" + (soNhanSu - 1);
                }

                String trangThai;
                if (soNhanSu <= 0) {
                    trangThai = "Cần phân công";
                } else if (tgBatDau != null && tgBatDau.isBefore(now)) {
                    trangThai = "Đang thực hiện";
                } else {
                    trangThai = "Sẵn sàng";
                }

                items.add(new DashboardTaskItemDTO(tenHM, tenSuKien, nguoiPhuTrach, tgBatDau, trangThai));
            }
            return items;
        }
    }
}
