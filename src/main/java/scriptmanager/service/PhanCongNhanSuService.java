package scriptmanager.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import scriptmanager.config.HibernateUtil;
import scriptmanager.entity.assignment.PhanCongNhanSu;
import scriptmanager.entity.assignment.pk.PhanCongNhanSuId;
import scriptmanager.entity.core.HangMucKichBan;
import scriptmanager.entity.user.NhanSu;
import scriptmanager.exception.BusinessRuleException;
import scriptmanager.exception.ValidationException;

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
            throw new ValidationException("Mã sự kiện và mã nhân sự phải hợp lệ.");
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            NhanSu nhanSu = session.get(NhanSu.class, maNS);
            if (nhanSu == null) {
                throw new ValidationException("Không tìm thấy nhân sự.");
            }

            // Lấy toàn bộ hạng mục của sự kiện để phân công đồng loạt.
            List<HangMucKichBan> hangMucs = session.createQuery(
                            "FROM HangMucKichBan hm WHERE hm.suKienTiec.maSK = :maSK ORDER BY hm.tgBatDau ASC",
                            HangMucKichBan.class)
                    .setParameter("maSK", maSK)
                    .list();

            if (hangMucs.isEmpty()) {
                throw new BusinessRuleException("Sự kiện chưa có hạng mục để phân công nhân sự.");
            }

            // Nếu đã có bản ghi thì cập nhật nhiệm vụ, chưa có thì tạo mới.
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

    public void assignNhanSuToHangMuc(int maSK, int maHM, int maNS, String nhiemVu) {
        AuthorizationService.requireManagerOrAdmin();

        String normalizedTask = nhiemVu == null ? "" : nhiemVu.trim();
        if (maSK <= 0 || maHM <= 0 || maNS <= 0) {
            throw new ValidationException("Sự kiện, hạng mục và nhân sự phải hợp lệ.");
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            NhanSu nhanSu = session.get(NhanSu.class, maNS);
            if (nhanSu == null) {
                throw new ValidationException("Không tìm thấy nhân sự.");
            }

            HangMucKichBan hangMuc = session.get(HangMucKichBan.class, maHM);
            if (hangMuc == null || hangMuc.getSuKienTiec() == null || hangMuc.getSuKienTiec().getMaSK() != maSK) {
                throw new ValidationException("Hạng mục không thuộc sự kiện đã chọn.");
            }

            // Mỗi hạng mục chỉ cho một người phụ trách chính.
            Long assignedToCurrentHangMuc = session.createQuery(
                            "SELECT COUNT(pc) FROM PhanCongNhanSu pc " +
                                    "WHERE pc.hangMuc.maHM = :maHM AND pc.nhanSu.maNS <> :maNS",
                            Long.class)
                    .setParameter("maHM", maHM)
                    .setParameter("maNS", maNS)
                    .uniqueResult();
            if (assignedToCurrentHangMuc != null && assignedToCurrentHangMuc > 0) {
                throw new BusinessRuleException("Hạng mục đã có người phụ trách, không thể phân công thêm.");
            }

            // Nhân sự đã có hạng mục khác thì không nhận thêm.
            List<Object[]> existingOtherAssignments = session.createQuery(
                            "SELECT pc.hangMuc.maHM, pc.hangMuc.tenHM " +
                                    "FROM PhanCongNhanSu pc " +
                                    "WHERE pc.nhanSu.maNS = :maNS AND pc.hangMuc.maHM <> :maHM",
                            Object[].class)
                    .setParameter("maNS", maNS)
                    .setParameter("maHM", maHM)
                    .setMaxResults(1)
                    .list();
            if (!existingOtherAssignments.isEmpty()) {
                Object[] row = existingOtherAssignments.get(0);
                throw new BusinessRuleException(
                        "Nhân sự đã được phân công ở hạng mục khác (" + row[1] + " - Mã " + row[0] + ")."
                );
            }

            // Upsert phân công theo cặp hạng mục - nhân sự.
            PhanCongNhanSuId id = new PhanCongNhanSuId(maHM, maNS);
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

