package scriptmanager.service;

import scriptmanager.dao.DoiTacDao;
import scriptmanager.dao.DoiTacDaoImpl;
import scriptmanager.entity.user.DoiTac;

import java.util.List;

public class DoiTacService {
    private final DoiTacDao doiTacDao;

    public DoiTacService() {
        this.doiTacDao = new DoiTacDaoImpl();
    }

    public List<DoiTac> findAll() {
        return doiTacDao.findAll();
    }

    public DoiTac findById(int id) {
        return doiTacDao.findById(id);
    }

    public void save(DoiTac item) {
        doiTacDao.save(item);
    }

    public void update(DoiTac item) {
        doiTacDao.update(item);
    }

    public void delete(int id) {
        DoiTac item = doiTacDao.findById(id);
        if (item != null) {
            doiTacDao.delete(item);
        }
    }
}

