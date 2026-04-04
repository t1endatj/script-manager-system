import os

files_to_write = {
    "src/main/java/scriptmanager/service/HangMucKichBanServiceImpl.java": """package scriptmanager.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import scriptmanager.config.HibernateUtil;
import scriptmanager.dao.HangMucKichBanDao;
import scriptmanager.dao.HangMucKichBanDaoImpl;
import scriptmanager.entity.core.HangMucKichBan;

import java.util.List;
import java.util.stream.Collectors;

public class HangMucKichBanServiceImpl implements HangMucKichBanService {
    private final HangMucKichBanDao dao;

    public HangMucKichBanServiceImpl() {
        this.dao = new HangMucKichBanDaoImpl();
    }

    @Override
    public List<HangMucKichBan> findAll() { 
        return dao.findAll(); 
    }

    @Override
    public List<HangMucKichBan> findBySuKienId(int suKienId) {
        return dao.findAll().stream()
                .filter(hm -> hm.getSuKienTiec() != null && hm.getSuKienTiec().getMaSK() == suKienId)
                .collect(Collectors.toList());
    }

    @Override
    public HangMucKichBan findById(int id) { 
        return dao.findById(id); 
    }

    @Override
    public void save(HangMucKichBan item) { 
        dao.save(item); 
    }

    @Override
    public void update(HangMucKichBan item) { 
        dao.update(item); 
    }

    @Override
    public void delete(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            
            session.createNativeQuery("DELETE FROM PhanCongNhanSu WHERE MaHM = :id").setParameter("id", id).executeUpdate();
            session.createNativeQuery("DELETE FROM PhanCongThietBi WHERE MaHM = :id").setParameter("id", id).executeUpdate();
            session.createNativeQuery("DELETE FROM SuDungDaoCu WHERE MaHM = :id").setParameter("id", id).executeUpdate();
            session.createNativeQuery("DELETE FROM SuDungHieuUng WHERE MaHM = :id").setParameter("id", id).executeUpdate();
            
            try {
                session.createNativeQuery("DELETE FROM DanhSachNhac WHERE MaHM = :id").setParameter("id", id).executeUpdate();
            } catch (Exception ignored) {}
            
            HangMucKichBan hm = session.get(HangMucKichBan.class, id);
            if (hm != null) {
                session.remove(hm);
            }
            
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
"""
}

for file_path, content in files_to_write.items():
    os.makedirs(os.path.dirname(file_path), exist_ok=True)
    with open(file_path, "w", encoding="utf-8") as f:
        f.write(content)