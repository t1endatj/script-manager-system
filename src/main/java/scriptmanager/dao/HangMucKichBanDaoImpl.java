package scriptmanager.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import scriptmanager.config.HibernateUtil;
import scriptmanager.entity.core.HangMucKichBan;

import java.util.List;

public class HangMucKichBanDaoImpl extends GenericDaoImpl<HangMucKichBan, Integer> implements HangMucKichBanDao {
    public HangMucKichBanDaoImpl() {
        super(HangMucKichBan.class);
    }

    @Override
    public List<HangMucKichBan> findByNguoiDungId(int maNguoiDung) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM HangMucKichBan hm WHERE hm.suKienTiec.nguoiDung.maND = :maND ORDER BY hm.tgBatDau ASC",
                            HangMucKichBan.class)
                    .setParameter("maND", maNguoiDung)
                    .list();
        }
    }

    @Override
    public void deleteByIdWithDependencies(int maHM) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Explicit deletes help when real DB schema is missing ON DELETE CASCADE.
            session.createMutationQuery("DELETE FROM DanhSachNhac n WHERE n.hangMuc.maHM = :maHM")
                    .setParameter("maHM", maHM)
                    .executeUpdate();
            session.createMutationQuery("DELETE FROM SuDungDaoCu s WHERE s.hangMuc.maHM = :maHM")
                    .setParameter("maHM", maHM)
                    .executeUpdate();
            session.createMutationQuery("DELETE FROM SuDungHieuUng s WHERE s.hangMuc.maHM = :maHM")
                    .setParameter("maHM", maHM)
                    .executeUpdate();
            session.createMutationQuery("DELETE FROM PhanCongThietBi p WHERE p.hangMuc.maHM = :maHM")
                    .setParameter("maHM", maHM)
                    .executeUpdate();
            session.createMutationQuery("DELETE FROM PhanCongNhanSu p WHERE p.hangMuc.maHM = :maHM")
                    .setParameter("maHM", maHM)
                    .executeUpdate();
            session.createMutationQuery("DELETE FROM HangMucKichBan hm WHERE hm.maHM = :maHM")
                    .setParameter("maHM", maHM)
                    .executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }
}