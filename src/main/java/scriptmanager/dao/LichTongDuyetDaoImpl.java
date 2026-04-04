package scriptmanager.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import scriptmanager.config.HibernateUtil;
import scriptmanager.entity.core.LichTongDuyet;

import java.util.List;

public class LichTongDuyetDaoImpl extends GenericDaoImpl<LichTongDuyet, Integer> implements LichTongDuyetDao {
    public LichTongDuyetDaoImpl() {
        super(LichTongDuyet.class);
    }

    @Override
    public List<LichTongDuyet> findByNguoiDungId(int maNguoiDung) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM LichTongDuyet ltd WHERE ltd.suKienTiec.nguoiDung.maND = :maND ORDER BY ltd.thoiGianDuyet DESC",
                            LichTongDuyet.class)
                    .setParameter("maND", maNguoiDung)
                    .list();
        }
    }

    @Override
    public void deleteById(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.createNativeQuery("DELETE FROM LichTongDuyet WHERE MaTongDuyet = :id")
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
}

