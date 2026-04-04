package scriptmanager.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import scriptmanager.config.HibernateUtil;
import scriptmanager.entity.asset.ThietBi;

public class ThietBiDaoImpl extends GenericDaoImpl<ThietBi, Integer> implements ThietBiDao {
    public ThietBiDaoImpl() {
        super(ThietBi.class);
    }

    @Override
    public boolean hasAssignments(int maThietBi) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery(
                            "select count(pc) from PhanCongThietBi pc where pc.thietBi.maTB = :maTB",
                            Long.class)
                    .setParameter("maTB", maThietBi)
                    .uniqueResult();
            return count != null && count > 0;
        }
    }

    @Override
    public void deleteById(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.createNativeQuery("DELETE FROM ThietBi WHERE MaTB = :id")
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

