package scriptmanager.dao;

import org.hibernate.Session;
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
}