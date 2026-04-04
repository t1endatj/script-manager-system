package scriptmanager.dao;

import org.hibernate.Session;
import scriptmanager.config.HibernateUtil;
import scriptmanager.entity.asset.DaoCu;

public class DaoCuDaoImpl extends GenericDaoImpl<DaoCu, Integer> implements DaoCuDao {
    public DaoCuDaoImpl() {
        super(DaoCu.class);
    }

    @Override
    public boolean hasUsages(int maDaoCu) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery(
                            "select count(sd) from SuDungDaoCu sd where sd.daoCu.maDaoCu = :maDaoCu",
                            Long.class)
                    .setParameter("maDaoCu", maDaoCu)
                    .uniqueResult();
            return count != null && count > 0;
        }
    }
}

