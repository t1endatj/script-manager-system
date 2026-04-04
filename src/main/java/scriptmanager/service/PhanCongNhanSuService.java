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

import java.time.LocalDateTime;
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

            Long nhanSuCount = session.createQuery(
                            "select count(ns.maNS) from NhanSu ns where ns.maNS = :maNS",
                            Long.class)
                    .setParameter("maNS", maNS)
                    .uniqueResult();
            if (nhanSuCount == null || nhanSuCount == 0) {
                throw new ValidationException("Không tìm thấy nhân sự.");
            }

            Object[] hangMucInfo = session.createQuery(
                            "select hm.tgBatDau, hm.tgKetThuc " +
                                    "from HangMucKichBan hm " +
                                    "where hm.maHM = :maHM and hm.suKienTiec.maSK = :maSK",
                            Object[].class)
                    .setParameter("maHM", maHM)
                    .setParameter("maSK", maSK)
                    .uniqueResult();
            if (hangMucInfo == null) {
                throw new ValidationException("Hạng mục không thuộc sự kiện đã chọn.");
            }
            LocalDateTime targetStart = (LocalDateTime) hangMucInfo[0];
            LocalDateTime targetEnd = (LocalDateTime) hangMucInfo[1];

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

            // Nhân sự có thể nhận nhiều hạng mục, chỉ chặn khi trùng giờ.
            List<Object[]> existingOtherAssignments = session.createQuery(
                            "SELECT pc.hangMuc.maHM, pc.hangMuc.tenHM, pc.hangMuc.tgBatDau, pc.hangMuc.tgKetThuc " +
                                    "FROM PhanCongNhanSu pc " +
                                    "WHERE pc.nhanSu.maNS = :maNS AND pc.hangMuc.maHM <> :maHM",
                            Object[].class)
                    .setParameter("maNS", maNS)
                    .setParameter("maHM", maHM)
                    .list();

            for (Object[] row : existingOtherAssignments) {
                LocalDateTime existingStart = (LocalDateTime) row[2];
                LocalDateTime existingEnd = (LocalDateTime) row[3];
                if (rangesOverlap(targetStart, targetEnd, existingStart, existingEnd)) {
                    throw new BusinessRuleException(
                            "Nhân sự bị trùng lịch với hạng mục " + row[1] + " (Mã " + row[0] + ")."
                    );
                }
            }

            // Upsert bằng query để tránh side effect từ entity graph/cascade.
            int updated = session.createMutationQuery(
                            "update PhanCongNhanSu pc set pc.nhiemVu = :nhiemVu " +
                                    "where pc.hangMuc.maHM = :maHM and pc.nhanSu.maNS = :maNS")
                    .setParameter("nhiemVu", normalizedTask)
                    .setParameter("maHM", maHM)
                    .setParameter("maNS", maNS)
                    .executeUpdate();
            if (updated == 0) {
                session.createNativeQuery(
                                "INSERT INTO PhanCongNhanSu (MaHM, MaNS, NhiemVu) VALUES (:maHM, :maNS, :nhiemVu)")
                        .setParameter("maHM", maHM)
                        .setParameter("maNS", maNS)
                        .setParameter("nhiemVu", normalizedTask)
                        .executeUpdate();
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
            session.createMutationQuery(
                            "delete from PhanCongNhanSu pc where pc.hangMuc.maHM = :maHM and pc.nhanSu.maNS = :maNS")
                    .setParameter("maHM", maHM)
                    .setParameter("maNS", maNS)
                    .executeUpdate();
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw ex;
        }
    }

    private boolean rangesOverlap(LocalDateTime startA, LocalDateTime endA,
                                  LocalDateTime startB, LocalDateTime endB) {
        if (startA == null || endA == null || startB == null || endB == null) {
            return false;
        }
        return startA.isBefore(endB) && endA.isAfter(startB);
    }
}

