package scriptmanager.dao;

import org.hibernate.Session;
import scriptmanager.config.HibernateUtil;
import scriptmanager.entity.user.DoiTac;

public class DoiTacDaoImpl extends GenericDaoImpl<DoiTac, Integer> implements DoiTacDao {
    public DoiTacDaoImpl() {
        super(DoiTac.class);
    }

    @Override
    public boolean existsByTenDonVi(String tenDonVi, Integer excludeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "select count(dt.maDT) from DoiTac dt where lower(trim(dt.tenDonVi)) = :ten";
            if (excludeId != null) {
                hql += " and dt.maDT <> :excludeId";
            }

            var query = session.createQuery(hql, Long.class)
                    .setParameter("ten", tenDonVi == null ? "" : tenDonVi.trim().toLowerCase());

            if (excludeId != null) {
                query.setParameter("excludeId", excludeId);
            }

            Long count = query.uniqueResult();
            return count != null && count > 0;
        }
    }
}

