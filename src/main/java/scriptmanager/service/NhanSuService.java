package scriptmanager.service;

import scriptmanager.dao.NhanSuDao;
import scriptmanager.dao.NhanSuDaoImpl;
import scriptmanager.entity.user.NhanSu;

import java.util.List;

public class NhanSuService {
    private final NhanSuDao nhanSuDao;

    public NhanSuService() {
        this.nhanSuDao = new NhanSuDaoImpl();
    }

    public List<NhanSu> findAll() {
        return nhanSuDao.findAll();
    }

    public NhanSu findById(int id) {
        return nhanSuDao.findById(id);
    }

    public void save(NhanSu item) {
        AuthorizationService.requireManagerOrAdmin();
        nhanSuDao.save(item);
    }

    public void update(NhanSu item) {
        AuthorizationService.requireManagerOrAdmin();
        nhanSuDao.update(item);
    }

    public void delete(int id) {
        AuthorizationService.requireManagerOrAdmin();
        NhanSu item = nhanSuDao.findById(id);
        if (item != null) {
            nhanSuDao.delete(item);
        }
    }
}

