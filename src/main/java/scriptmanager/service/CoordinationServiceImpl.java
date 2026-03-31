package scriptmanager.service;

import scriptmanager.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoordinationServiceImpl implements CoordinationService {

    @Override
    public List<Map<String, Object>> getFullSchedule() {
        List<Map<String, Object>> boardData = new ArrayList<>();
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT hm.tenHM, hm.tgBatDau, hm.tgKetThuc, " +
                         "(SELECT COUNT(pcns) FROM hm.phanCongNhanSus pcns), " +
                         "(SELECT COUNT(pctb) FROM hm.phanCongThietBis pctb) " +
                         "FROM HangMucKichBan hm " +
                         "ORDER BY hm.tgBatDau ASC";

            Query<Object[]> query = session.createQuery(hql, Object[].class);
            List<Object[]> results = query.list();

            for (Object[] row : results) {
                Map<String, Object> item = new HashMap<>();
                item.put("tenHangMuc", row[0]);
                item.put("batDau", row[1]);
                item.put("ketThuc", row[2]);
                item.put("soNhanSu", row[3]);
                item.put("soThietBi", row[4]);
                boardData.add(item);
            }
        }
        return boardData;
    }

    @Override
    public List<Map<String, Object>> getResourceUsageByTime() {
        List<Map<String, Object>> usageData = new ArrayList<>();
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT ns.tenNS, hm.tenHM, hm.tgBatDau, hm.tgKetThuc " +
                         "FROM PhanCongNhanSu pc " +
                         "JOIN pc.nhanSu ns " +
                         "JOIN pc.hangMuc hm " +
                         "ORDER BY ns.tenNS, hm.tgBatDau";

            Query<Object[]> query = session.createQuery(hql, Object[].class);
            List<Object[]> results = query.list();

            for (Object[] row : results) {
                Map<String, Object> usage = new HashMap<>();
                usage.put("tenNhanSu", row[0]);
                usage.put("tenHangMuc", row[1]);
                usage.put("thoiGian", row[2] + " - " + row[3]);
                usageData.add(usage);
            }
        }
        return usageData;
    }
}
