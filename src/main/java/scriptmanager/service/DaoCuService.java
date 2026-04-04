package scriptmanager.service;

import scriptmanager.dao.DaoCuDao;
import scriptmanager.dao.DaoCuDaoImpl;
import scriptmanager.entity.asset.DaoCu;
import scriptmanager.exception.BusinessRuleException;

import java.util.List;

public class DaoCuService {
    private final DaoCuDao daoCuDao;

    public DaoCuService() {
        this.daoCuDao = new DaoCuDaoImpl();
    }

    public List<DaoCu> findAll() {
        return daoCuDao.findAll();
    }

    public DaoCu findById(int id) {
        return daoCuDao.findById(id);
    }

    public void save(DaoCu item) {
        AuthorizationService.requireManagerOrAdmin();
        daoCuDao.save(item);
    }

    public void update(DaoCu item) {
        AuthorizationService.requireManagerOrAdmin();
        daoCuDao.update(item);
    }

    public void delete(int id) {
        AuthorizationService.requireManagerOrAdmin();
        DaoCu item = daoCuDao.findById(id);
        if (item != null) {
            if (daoCuDao.hasUsages(id)) {
                throw new BusinessRuleException("Không thể xóa đạo cụ đang được sử dụng. Vui lòng gỡ phân bổ trước.");
            }
            daoCuDao.delete(item);
        }
    }
}

