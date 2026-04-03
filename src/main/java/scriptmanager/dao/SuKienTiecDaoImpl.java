package scriptmanager.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import scriptmanager.config.HibernateUtil;
import scriptmanager.entity.core.SuKienTiec;

import java.time.LocalDateTime;

public class SuKienTiecDaoImpl extends GenericDaoImpl<SuKienTiec, Integer> implements SuKienTiecDao {
    public SuKienTiecDaoImpl() {
        super(SuKienTiec.class);
    }

    @Override
    public void deleteById(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            session.createMutationQuery(
                    "delete from PhanCongNhanSu pc where pc.hangMuc.maHM in " +
                            "(select hm.maHM from HangMucKichBan hm where hm.suKienTiec.maSK = :id)"
            ).setParameter("id", id).executeUpdate();

            session.createMutationQuery(
                    "delete from PhanCongThietBi pc where pc.hangMuc.maHM in " +
                            "(select hm.maHM from HangMucKichBan hm where hm.suKienTiec.maSK = :id)"
            ).setParameter("id", id).executeUpdate();

            session.createMutationQuery(
                    "delete from SuDungDaoCu sd where sd.hangMuc.maHM in " +
                            "(select hm.maHM from HangMucKichBan hm where hm.suKienTiec.maSK = :id)"
            ).setParameter("id", id).executeUpdate();

            session.createMutationQuery(
                    "delete from SuDungHieuUng sd where sd.hangMuc.maHM in " +
                            "(select hm.maHM from HangMucKichBan hm where hm.suKienTiec.maSK = :id)"
            ).setParameter("id", id).executeUpdate();

            session.createMutationQuery(
                    "delete from DanhSachNhac ds where ds.hangMuc.maHM in " +
                            "(select hm.maHM from HangMucKichBan hm where hm.suKienTiec.maSK = :id)"
            ).setParameter("id", id).executeUpdate();

            session.createMutationQuery("delete from LichTongDuyet ltd where ltd.suKienTiec.maSK = :id")
                    .setParameter("id", id)
                    .executeUpdate();

            session.createMutationQuery("delete from HangMucKichBan hm where hm.suKienTiec.maSK = :id")
                    .setParameter("id", id)
                    .executeUpdate();

            session.createMutationQuery("delete from SuKienTiec sk where sk.maSK = :id")
                    .setParameter("id", id)
                    .executeUpdate();

            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            throw ex;
        }
    }

    @Override
    public boolean existsDuplicate(String tenSuKien, LocalDateTime thoiGianToChuc, String diaDiem, int maNguoiDung, Integer excludeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "select count(sk.maSK) from SuKienTiec sk " +
                    "where lower(trim(sk.tenSuKien)) = :ten " +
                    "and sk.thoiGianToChuc = :thoiGian " +
                    "and lower(trim(coalesce(sk.diaDiem, ''))) = :diaDiem " +
                    "and sk.nguoiDung.maND = :maND";

            if (excludeId != null) {
                hql += " and sk.maSK <> :excludeId";
            }

            var query = session.createQuery(hql, Long.class)
                    .setParameter("ten", tenSuKien == null ? "" : tenSuKien.trim().toLowerCase())
                    .setParameter("thoiGian", thoiGianToChuc)
                    .setParameter("diaDiem", diaDiem == null ? "" : diaDiem.trim().toLowerCase())
                    .setParameter("maND", maNguoiDung);

            if (excludeId != null) {
                query.setParameter("excludeId", excludeId);
            }

            Long count = query.uniqueResult();
            return count != null && count > 0;
        }
    }
}