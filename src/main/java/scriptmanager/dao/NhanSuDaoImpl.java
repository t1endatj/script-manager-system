package scriptmanager.dao;

import org.hibernate.Session;
import scriptmanager.config.HibernateUtil;
import scriptmanager.entity.user.NhanSu;

public class NhanSuDaoImpl extends GenericDaoImpl<NhanSu, Integer> implements NhanSuDao {
    public NhanSuDaoImpl() {
        super(NhanSu.class);
    }

    @Override
    public boolean hasAssignments(int maNhanSu) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery(
                            "select count(pc) from PhanCongNhanSu pc where pc.nhanSu.maNS = :maNS",
                            Long.class)
                    .setParameter("maNS", maNhanSu)
                    .uniqueResult();
            return count != null && count > 0;
        }
    }
}

