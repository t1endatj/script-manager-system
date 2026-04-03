package scriptmanager.service;

import scriptmanager.dao.ThietBiDao;
import scriptmanager.dao.ThietBiDaoImpl;
import scriptmanager.entity.asset.ThietBi;

import java.util.List;

public class ThietBiService {
    private final ThietBiDao thietBiDao;

    public ThietBiService() {
        this.thietBiDao = new ThietBiDaoImpl();
    }

    public List<ThietBi> findAll() {
        return thietBiDao.findAll();
    }

    public ThietBi findById(int id) {
        return thietBiDao.findById(id);
    }

    public void save(ThietBi item) {
        thietBiDao.save(item);
    }

    public void update(ThietBi item) {
        thietBiDao.update(item);
    }

    public void delete(int id) {
        ThietBi item = thietBiDao.findById(id);
        if (item != null) {
            thietBiDao.delete(item);
        }
    }
}

