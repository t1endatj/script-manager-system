package scriptmanager.service;

import scriptmanager.dao.HieuUngDao;
import scriptmanager.dao.HieuUngDaoImpl;
import scriptmanager.entity.asset.HieuUng;

import java.util.List;

public class HieuUngService {
    private final HieuUngDao hieuUngDao;

    public HieuUngService() {
        this.hieuUngDao = new HieuUngDaoImpl();
    }

    public List<HieuUng> findAll() {
        return hieuUngDao.findAll();
    }

    public HieuUng findById(int id) {
        return hieuUngDao.findById(id);
    }

    public void save(HieuUng item) {
        hieuUngDao.save(item);
    }

    public void update(HieuUng item) {
        hieuUngDao.update(item);
    }

    public void delete(int id) {
        HieuUng item = hieuUngDao.findById(id);
        if (item != null) {
            hieuUngDao.delete(item);
        }
    }
}

