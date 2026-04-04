package scriptmanager.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import scriptmanager.config.HibernateUtil;
import scriptmanager.entity.assignment.*;
import scriptmanager.entity.assignment.pk.*;
import scriptmanager.entity.core.HangMucKichBan;
import java.util.ArrayList;
import java.util.List;

public class AssignmentServiceImpl implements AssignmentService {

    @Override
    public List<PhanCongNhanSu> getNhanSuByHangMuc(int hangMucId) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("FROM PhanCongNhanSu p JOIN FETCH p.nhanSu WHERE p.id.maHM = :hm", PhanCongNhanSu.class)
                    .setParameter("hm", hangMucId).list();
        }
    }

    @Override
    public List<PhanCongThietBi> getThietBiByHangMuc(int hangMucId) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("FROM PhanCongThietBi p JOIN FETCH p.thietBi WHERE p.id.maHM = :hm", PhanCongThietBi.class)
                    .setParameter("hm", hangMucId).list();
        }
    }

    @Override
    public List<SuDungDaoCu> getDaoCuByHangMuc(int hangMucId) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("FROM SuDungDaoCu p JOIN FETCH p.daoCu WHERE p.id.maHM = :hm", SuDungDaoCu.class)
                    .setParameter("hm", hangMucId).list();
        }
    }

    @Override
    public List<SuDungHieuUng> getHieuUngByHangMuc(int hangMucId) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("FROM SuDungHieuUng p JOIN FETCH p.hieuUng WHERE p.id.maHM = :hm", SuDungHieuUng.class)
                    .setParameter("hm", hangMucId).list();
        }
    }

    @Override
    public void assignNhanSu(PhanCongNhanSu pc) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            Long count = s.createQuery("SELECT count(p) FROM PhanCongNhanSu p WHERE p.id.maHM = :hm AND p.id.maNS = :ns", Long.class)
                    .setParameter("hm", pc.getId().getMaHM())
                    .setParameter("ns", pc.getId().getMaNS())
                    .uniqueResult();
            if (count != null && count > 0) {
                s.createNativeQuery("UPDATE PhanCongNhanSu SET NhiemVu = :nv WHERE MaHM = :hm AND MaNS = :ns")
                        .setParameter("nv", pc.getNhiemVu())
                        .setParameter("hm", pc.getId().getMaHM())
                        .setParameter("ns", pc.getId().getMaNS())
                        .executeUpdate();
            } else {
                s.createNativeQuery("INSERT INTO PhanCongNhanSu (MaHM, MaNS, NhiemVu) VALUES (:hm, :ns, :nv)")
                        .setParameter("hm", pc.getId().getMaHM())
                        .setParameter("ns", pc.getId().getMaNS())
                        .setParameter("nv", pc.getNhiemVu())
                        .executeUpdate();
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void assignThietBi(PhanCongThietBi pc) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            Long count = s.createQuery("SELECT count(p) FROM PhanCongThietBi p WHERE p.id.maHM = :hm AND p.id.maTB = :tb", Long.class)
                    .setParameter("hm", pc.getId().getMaHM())
                    .setParameter("tb", pc.getId().getMaTB())
                    .uniqueResult();
            if (count != null && count > 0) {
                s.createNativeQuery("UPDATE PhanCongThietBi SET SoLuongSuDung = :sl WHERE MaHM = :hm AND MaTB = :tb")
                        .setParameter("sl", pc.getSoLuongSuDung())
                        .setParameter("hm", pc.getId().getMaHM())
                        .setParameter("tb", pc.getId().getMaTB())
                        .executeUpdate();
            } else {
                s.createNativeQuery("INSERT INTO PhanCongThietBi (MaHM, MaTB, SoLuongSuDung) VALUES (:hm, :tb, :sl)")
                        .setParameter("hm", pc.getId().getMaHM())
                        .setParameter("tb", pc.getId().getMaTB())
                        .setParameter("sl", pc.getSoLuongSuDung())
                        .executeUpdate();
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void assignDaoCu(SuDungDaoCu pc) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            Long count = s.createQuery("SELECT count(p) FROM SuDungDaoCu p WHERE p.id.maHM = :hm AND p.id.maDaoCu = :dc", Long.class)
                    .setParameter("hm", pc.getId().getMaHM())
                    .setParameter("dc", pc.getId().getMaDaoCu())
                    .uniqueResult();
            if (count != null && count > 0) {
                s.createNativeQuery("UPDATE SuDungDaoCu SET SoLuongSuDung = :sl WHERE MaHM = :hm AND MaDaoCu = :dc")
                        .setParameter("sl", pc.getSoLuongSuDung())
                        .setParameter("hm", pc.getId().getMaHM())
                        .setParameter("dc", pc.getId().getMaDaoCu())
                        .executeUpdate();
            } else {
                s.createNativeQuery("INSERT INTO SuDungDaoCu (MaHM, MaDaoCu, SoLuongSuDung) VALUES (:hm, :dc, :sl)")
                        .setParameter("hm", pc.getId().getMaHM())
                        .setParameter("dc", pc.getId().getMaDaoCu())
                        .setParameter("sl", pc.getSoLuongSuDung())
                        .executeUpdate();
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void assignHieuUng(SuDungHieuUng pc) {
        try(Session s = HibernateUtil.getSessionFactory().openSession()){
            Transaction tx = s.beginTransaction();
            s.merge(pc);
            tx.commit();
        }
    }

    @Override
    public void removeNhanSu(PhanCongNhanSuId id) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            s.createNativeQuery("DELETE FROM PhanCongNhanSu WHERE MaHM = :hm AND MaNS = :ns")
             .setParameter("hm", id.getMaHM()).setParameter("ns", id.getMaNS()).executeUpdate();
            tx.commit();
        } catch(Exception e) { if(tx!=null) tx.rollback(); throw e; }
    }

    @Override
    public void removeThietBi(PhanCongThietBiId id) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            s.createNativeQuery("DELETE FROM PhanCongThietBi WHERE MaHM = :hm AND MaTB = :tb")
             .setParameter("hm", id.getMaHM()).setParameter("tb", id.getMaTB()).executeUpdate();
            tx.commit();
        } catch(Exception e) { if(tx!=null) tx.rollback(); throw e; }
    }

    @Override
    public void removeDaoCu(SuDungDaoCuId id) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            s.createNativeQuery("DELETE FROM SuDungDaoCu WHERE MaHM = :hm AND MaDaoCu = :dc")
             .setParameter("hm", id.getMaHM()).setParameter("dc", id.getMaDaoCu()).executeUpdate();
            tx.commit();
        } catch(Exception e) { if(tx!=null) tx.rollback(); throw e; }
    }

    @Override
    public void removeHieuUng(SuDungHieuUngId id) {
        try(Session s = HibernateUtil.getSessionFactory().openSession()){
            Transaction tx = s.beginTransaction();
            SuDungHieuUng item = s.get(SuDungHieuUng.class, id);
            if(item != null) s.remove(item);
            tx.commit();
        }
    }

    @Override
    public List<String> checkConflicts(HangMucKichBan hangMuc) {
        List<String> conflicts = new ArrayList<>();
        if (hangMuc.getTgBatDau() == null || hangMuc.getTgKetThuc() == null) return conflicts;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<PhanCongNhanSu> nss = session.createQuery("FROM PhanCongNhanSu p JOIN FETCH p.nhanSu WHERE p.id.maHM = :hm", PhanCongNhanSu.class)
                    .setParameter("hm", hangMuc.getMaHM()).list();
            for (PhanCongNhanSu pc : nss) {
                String hql = "SELECT hm FROM HangMucKichBan hm JOIN hm.phanCongNhanSus pcns " +
                             "WHERE pcns.nhanSu.maNS = :maNS AND hm.maHM != :currentHM " +
                             "AND (:start < hm.tgKetThuc AND :end > hm.tgBatDau)";
                Query<HangMucKichBan> query = session.createQuery(hql, HangMucKichBan.class);
                query.setParameter("maNS", pc.getNhanSu().getMaNS());
                query.setParameter("currentHM", hangMuc.getMaHM());
                query.setParameter("start", hangMuc.getTgBatDau());
                query.setParameter("end", hangMuc.getTgKetThuc());
                List<HangMucKichBan> overlapHMs = query.list();
                if (!overlapHMs.isEmpty()) {
                    conflicts.add("Nhân sự " + pc.getNhanSu().getTenNS() + " trùng lịch với HM: " + overlapHMs.get(0).getTenHM());
                }
            }

            List<PhanCongThietBi> tbs = session.createQuery("FROM PhanCongThietBi p JOIN FETCH p.thietBi WHERE p.id.maHM = :hm", PhanCongThietBi.class)
                    .setParameter("hm", hangMuc.getMaHM()).list();
            for (PhanCongThietBi pc : tbs) {
                String hqlSum = "SELECT SUM(pctb.soLuongSuDung) FROM PhanCongThietBi pctb JOIN pctb.hangMuc hm " +
                                "WHERE pctb.thietBi.maTB = :maTB AND hm.maHM != :currentHM " +
                                "AND (:start < hm.tgKetThuc AND :end > hm.tgBatDau)";
                Query<Long> querySum = session.createQuery(hqlSum, Long.class);
                querySum.setParameter("maTB", pc.getThietBi().getMaTB());
                querySum.setParameter("currentHM", hangMuc.getMaHM());
                querySum.setParameter("start", hangMuc.getTgBatDau());
                querySum.setParameter("end", hangMuc.getTgKetThuc());
                Long usedQuantity = querySum.uniqueResult();
                if (usedQuantity == null) usedQuantity = 0L;
                int totalAvailable = pc.getThietBi().getSoLuong();
                if (usedQuantity + pc.getSoLuongSuDung() > totalAvailable) {
                    conflicts.add("Thiết bị " + pc.getThietBi().getTenTB() + " không đủ (Cần: " + (usedQuantity + pc.getSoLuongSuDung()) + ", Có: " + totalAvailable + ")");
                }
            }
        } catch(Exception e) { e.printStackTrace(); }
        return conflicts;
    }
}
