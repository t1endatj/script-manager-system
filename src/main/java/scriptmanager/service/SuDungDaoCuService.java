package scriptmanager.service;

import scriptmanager.dao.SuDungDaoCuDao;
import scriptmanager.dao.SuDungDaoCuDaoImpl;
import scriptmanager.entity.assignment.SuDungDaoCu;
import scriptmanager.entity.assignment.pk.SuDungDaoCuId;

import java.util.List;

public class SuDungDaoCuService {
    private final SuDungDaoCuDao suDungDaoCuDao;

    public SuDungDaoCuService() {
        this.suDungDaoCuDao = new SuDungDaoCuDaoImpl();
    }

    public List<SuDungDaoCu> findAll() {
        return suDungDaoCuDao.findAll();
    }

    public SuDungDaoCu findById(SuDungDaoCuId id) {
        return suDungDaoCuDao.findById(id);
    }

    public void save(SuDungDaoCu item) {
        suDungDaoCuDao.save(item);
    }

    public void update(SuDungDaoCu item) {
        suDungDaoCuDao.update(item);
    }

    public void delete(SuDungDaoCuId id) {
        SuDungDaoCu item = suDungDaoCuDao.findById(id);
        if (item != null) {
            suDungDaoCuDao.delete(item);
        }
    }
}

