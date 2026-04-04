package scriptmanager.service;

import org.hibernate.Session;
import org.hibernate.query.Query;
import scriptmanager.config.HibernateUtil;
import scriptmanager.config.UserSession;
import scriptmanager.enums.UserRole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoordinationServiceImpl implements CoordinationService {

    @Override
    public List<Map<String, Object>> getFullSchedule() {
        List<Map<String, Object>> boardData = new ArrayList<>();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Integer currentUserId = UserSession.getCurrentUserId();
            boolean scopedToUser = UserSession.getCurrentRole() == UserRole.USER && currentUserId != null;

            String hql = scopedToUser
                    ? "SELECT hm.tenHM, sk.tenSuKien, hm.tgBatDau, hm.tgKetThuc, " +
                      "(SELECT COUNT(pcns) FROM hm.phanCongNhanSus pcns) " +
                      "FROM HangMucKichBan hm " +
                      "JOIN hm.suKienTiec sk " +
                      "WHERE sk.nguoiDung.maND = :maND " +
                      "ORDER BY hm.tgBatDau ASC"
                    : "SELECT hm.tenHM, sk.tenSuKien, hm.tgBatDau, hm.tgKetThuc, " +
                      "(SELECT COUNT(pcns) FROM hm.phanCongNhanSus pcns) " +
                      "FROM HangMucKichBan hm " +
                      "JOIN hm.suKienTiec sk " +
                      "ORDER BY hm.tgBatDau ASC";

            Query<Object[]> query = session.createQuery(hql, Object[].class);
            if (scopedToUser) {
                query.setParameter("maND", currentUserId);
            }

            for (Object[] row : query.list()) {
                Map<String, Object> item = new HashMap<>();
                item.put("tenHangMuc", row[0]);
                item.put("tenSuKien", row[1]);
                item.put("batDau", row[2]);
                item.put("ketThuc", row[3]);
                item.put("soNhanSu", row[4]);
                boardData.add(item);
            }
        }

        return boardData;
    }

    @Override
    public List<Map<String, Object>> getResourceUsageByTime() {
        List<Map<String, Object>> usageData = new ArrayList<>();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Integer currentUserId = UserSession.getCurrentUserId();
            boolean scopedToUser = UserSession.getCurrentRole() == UserRole.USER && currentUserId != null;

            String hql = scopedToUser
                    ? "SELECT ns.tenNS, hm.tenHM, sk.tenSuKien, hm.tgBatDau, hm.tgKetThuc, pc.nhiemVu " +
                      "FROM PhanCongNhanSu pc " +
                      "JOIN pc.nhanSu ns " +
                      "JOIN pc.hangMuc hm " +
                      "JOIN hm.suKienTiec sk " +
                      "WHERE sk.nguoiDung.maND = :maND " +
                      "ORDER BY sk.tenSuKien, hm.tgBatDau, ns.tenNS"
                    : "SELECT ns.tenNS, hm.tenHM, sk.tenSuKien, hm.tgBatDau, hm.tgKetThuc, pc.nhiemVu " +
                      "FROM PhanCongNhanSu pc " +
                      "JOIN pc.nhanSu ns " +
                      "JOIN pc.hangMuc hm " +
                      "JOIN hm.suKienTiec sk " +
                      "ORDER BY sk.tenSuKien, hm.tgBatDau, ns.tenNS";

            Query<Object[]> query = session.createQuery(hql, Object[].class);
            if (scopedToUser) {
                query.setParameter("maND", currentUserId);
            }

            for (Object[] row : query.list()) {
                Map<String, Object> usage = new HashMap<>();
                usage.put("tenNhanSu", row[0]);
                usage.put("tenHangMuc", row[1]);
                usage.put("tenSuKien", row[2]);
                usage.put("thoiGian", row[3] + " - " + row[4]);
                usage.put("nhiemVu", row[5] == null ? "" : row[5]);
                usageData.add(usage);
            }
        }

        return usageData;
    }
}
