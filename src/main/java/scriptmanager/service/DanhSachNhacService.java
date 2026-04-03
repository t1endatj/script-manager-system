package scriptmanager.service;

import scriptmanager.dao.DanhSachNhacDao;
import scriptmanager.dao.DanhSachNhacDaoImpl;
import scriptmanager.entity.asset.DanhSachNhac;

import java.util.List;

public class DanhSachNhacService {
    private final DanhSachNhacDao danhSachNhacDao;

    public DanhSachNhacService() {
        this.danhSachNhacDao = new DanhSachNhacDaoImpl();
    }

    public List<DanhSachNhac> findAll() {
        return danhSachNhacDao.findAll();
    }

    public DanhSachNhac findById(int id) {
        return danhSachNhacDao.findById(id);
    }

    public void save(DanhSachNhac item) {
        danhSachNhacDao.save(item);
    }

    public void update(DanhSachNhac item) {
        danhSachNhacDao.update(item);
    }

    public void delete(int id) {
        DanhSachNhac item = danhSachNhacDao.findById(id);
        if (item != null) {
            danhSachNhacDao.delete(item);
        }
    }
}

