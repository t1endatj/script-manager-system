package scriptmanager.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import scriptmanager.config.HibernateUtil;
import scriptmanager.entity.assignment.PhanCongNhanSu;
import scriptmanager.entity.assignment.pk.PhanCongNhanSuId;
import scriptmanager.entity.core.HangMucKichBan;
import scriptmanager.entity.user.NhanSu;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PhanCongNhanSuService {

    public List<Map<String, Object>> findAllView() {
        List<Map<String, Object>> rows = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT sk.maSK, sk.tenSuKien, hm.maHM, hm.tenHM, ns.maNS, ns.tenNS, pc.nhiemVu " +
                    "FROM PhanCongNhanSu pc " +
                    "JOIN pc.hangMuc hm " +
                    "JOIN hm.suKienTiec sk " +
                    "JOIN pc.nhanSu ns " +
                    "ORDER BY sk.maSK, hm.tgBatDau, ns.tenNS";
            for (Object[] row : session.createQuery(hql, Object[].class).list()) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("maSK", row[0]);
                item.put("tenSuKien", row[1]);
                item.put("maHM", row[2]);
                item.put("tenHangMuc", row[3]);
                item.put("maNS", row[4]);
                item.put("tenNhanSu", row[5]);
                item.put("nhiemVu", row[6]);
                rows.add(item);
            }
        }
        return rows;
    }

    public void assignNhanSuToEvent(int maSK, int maNS, String nhiemVu) {
        AuthorizationService.requireManagerOrAdmin();

        String normalizedTask = nhiemVu == null ? "" : nhiemVu.trim();
        if (maSK <= 0 || maNS <= 0) {
            throw new IllegalArgumentException("Mã sự kiện và mã nhân sự phải hợp lệ.");
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            NhanSu nhanSu = session.get(NhanSu.class, maNS);
            if (nhanSu == null) {
                throw new IllegalArgumentException("Không tìm thấy nhân sự.");
            }

            List<HangMucKichBan> hangMucs = session.createQuery(
                            "FROM HangMucKichBan hm WHERE hm.suKienTiec.maSK = :maSK ORDER BY hm.tgBatDau ASC",
                            HangMucKichBan.class)
                    .setParameter("maSK", maSK)
                    .list();

            if (hangMucs.isEmpty()) {
                throw new IllegalArgumentException("Sự kiện chưa có hạng mục để phân công nhân sự.");
            }

            for (HangMucKichBan hangMuc : hangMucs) {
                PhanCongNhanSuId id = new PhanCongNhanSuId(hangMuc.getMaHM(), maNS);
                PhanCongNhanSu existing = session.get(PhanCongNhanSu.class, id);
                if (existing == null) {
                    PhanCongNhanSu created = new PhanCongNhanSu();
                    created.setHangMuc(hangMuc);
                    created.setNhanSu(nhanSu);
                    created.setNhiemVu(normalizedTask);
                    session.persist(created);
                } else {
                    existing.setNhiemVu(normalizedTask);
                }
            }

            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw ex;
        }
    }

    public void deleteByHangMucAndNhanSu(int maHM, int maNS) {
        AuthorizationService.requireManagerOrAdmin();
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            PhanCongNhanSu item = session.get(PhanCongNhanSu.class, new PhanCongNhanSuId(maHM, maNS));
            if (item != null) {
                session.remove(item);
            }
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw ex;
        }
    }
}

