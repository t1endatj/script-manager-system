package scriptmanager.dao;

import org.hibernate.Session;
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
}

