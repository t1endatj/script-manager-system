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
        validate(item, null);
        doiTacDao.save(item);
    }

    public void update(DoiTac item) {
        Integer excludeId = item != null ? item.getMaDT() : null;
        validate(item, excludeId);
        doiTacDao.update(item);
    }

    public void delete(int id) {
        DoiTac item = doiTacDao.findById(id);
        if (item != null) {
            doiTacDao.delete(item);
        }
    }

    private void validate(DoiTac item, Integer excludeId) {
        if (item == null) {
            throw new IllegalArgumentException("Dữ liệu đối tác không hợp lệ.");
        }

        String tenDonVi = item.getTenDonVi() == null ? "" : item.getTenDonVi().trim();
        if (tenDonVi.isEmpty()) {
            throw new IllegalArgumentException("Tên đơn vị không được để trống.");
        }

        if (doiTacDao.existsByTenDonVi(tenDonVi, excludeId)) {
            throw new IllegalArgumentException("Đối tác đã tồn tại.");
        }

        item.setTenDonVi(tenDonVi);
        item.setLinhVuc(item.getLinhVuc() == null ? "" : item.getLinhVuc().trim());
        item.setSdt(item.getSdt() == null ? "" : item.getSdt().trim());
    }
}

