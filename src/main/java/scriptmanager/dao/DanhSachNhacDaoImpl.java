package scriptmanager.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import scriptmanager.config.HibernateUtil;
import scriptmanager.entity.asset.DanhSachNhac;

public class DanhSachNhacDaoImpl extends GenericDaoImpl<DanhSachNhac, Integer> implements DanhSachNhacDao {
    public DanhSachNhacDaoImpl() {
        super(DanhSachNhac.class);
    }

    @Override
    public void deleteById(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.createNativeQuery("DELETE FROM DanhSachNhac WHERE MaBaiHat = :id")
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

