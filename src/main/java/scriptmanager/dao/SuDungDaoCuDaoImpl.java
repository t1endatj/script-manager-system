package scriptmanager.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import scriptmanager.config.HibernateUtil;
import scriptmanager.entity.assignment.SuDungDaoCu;
import scriptmanager.entity.assignment.pk.SuDungDaoCuId;

public class SuDungDaoCuDaoImpl extends GenericDaoImpl<SuDungDaoCu, SuDungDaoCuId> implements SuDungDaoCuDao {
    public SuDungDaoCuDaoImpl() {
        super(SuDungDaoCu.class);
    }

    @Override
    public void deleteById(SuDungDaoCuId id) {
        if (id == null) {
            return;
        }
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.createNativeQuery("DELETE FROM SuDungDaoCu WHERE MaHM = :maHM AND MaDaoCu = :maDaoCu")
                    .setParameter("maHM", id.getMaHM())
                    .setParameter("maDaoCu", id.getMaDaoCu())
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

