package scriptmanager.service;

import scriptmanager.dao.LichTongDuyetDao;
import scriptmanager.dao.LichTongDuyetDaoImpl;
import scriptmanager.entity.core.LichTongDuyet;

import java.util.List;

public class LichTongDuyetService {
    private final LichTongDuyetDao lichTongDuyetDao;

    public LichTongDuyetService() {
        this.lichTongDuyetDao = new LichTongDuyetDaoImpl();
    }

    public List<LichTongDuyet> findAll() {
        return lichTongDuyetDao.findAll();
    }

    public LichTongDuyet findById(int id) {
        return lichTongDuyetDao.findById(id);
    }

    public void save(LichTongDuyet item) {
        lichTongDuyetDao.save(item);
    }

    public void update(LichTongDuyet item) {
        lichTongDuyetDao.update(item);
    }

    public void delete(int id) {
        LichTongDuyet item = lichTongDuyetDao.findById(id);
        if (item != null) {
            lichTongDuyetDao.delete(item);
        }
    }
}

